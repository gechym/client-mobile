package com.example.dacs3.ui.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.media.audiofx.LoudnessEnhancer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.GestureDetectorCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dacs3.R
import com.example.dacs3.databinding.ActivityHls2Binding
import com.example.dacs3.databinding.BoosterBinding
import com.example.dacs3.databinding.MoreFeaturesBinding
import com.example.dacs3.databinding.SpeedDialogBinding
import com.github.vkay94.dtpv.youtube.YouTubeOverlay
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.collect.ImmutableList
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.system.exitProcess

class HlsActivity : AppCompatActivity() , GestureDetector.OnGestureListener  {

    lateinit var binding : ActivityHls2Binding

    //TODO khai báo các biến của exoplayer
    lateinit var constraintLayoutRoot : ConstraintLayout
    lateinit var exoPlayerView : PlayerView
    private lateinit var simpleExoPlayer : SimpleExoPlayer
    lateinit var mediaSource: MediaSource
    lateinit var urlType: URLType

    lateinit var urlMedia : String
    lateinit var subUrl : String


    companion object{

        lateinit var runnable : Runnable

        private var repeat : Boolean = false


        //TÍNH NĂNG AUDIO TRACK
        private lateinit var trackSelector: DefaultTrackSelector

        //TÍNH NĂNG SUBTITLE
        private var isSubtitle: Boolean = true

        //AUDIO BUTTER
        lateinit var loudnessEnhancer: LoudnessEnhancer


        //SPEED CHANGE
        private var speed: Float = 1.0f

        //SPEEL TIME
        private var timer : Timer? = null

        // Tăng giảm âm thanh độ sáng bằng cách vuốt
        private lateinit var gestureDetectorCompat: GestureDetectorCompat

        // Biến của giá trị
        private var brightness : Int = 0


        // vulume
        private var volume : Int = 4

        // audio manager
        private lateinit var audioManager : AudioManager

        // isloke
        private var isLocked : Boolean = false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHls2Binding.inflate(layoutInflater)

        //fix các góc bo của màng hình chế độ toàn màng hình
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(binding.root)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
//        }
//
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        WindowInsetsControllerCompat(window,binding.root).let { controller ->
//            controller.hide(WindowInsetsCompat.Type.statusBars())
//            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        urlMedia = intent.getStringExtra("urlMedia").toString()
        subUrl = intent.getStringExtra("subUrl").toString()



        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        findView()
        initPlayer()
    }


    private fun findView(){
        constraintLayoutRoot = findViewById(R.id.constraintLayoutRoot)
        exoPlayerView = findViewById(R.id.exoPlayerView)
    }


    // TODO KHỞI TẠO TRÌNH PLAYER BAN ĐẦU
    private fun initPlayer(){
        gestureDetectorCompat = GestureDetectorCompat(this, this, )
        trackSelector = DefaultTrackSelector(this)//TODO AUDIO TRACK
        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector) //TODO AUDIO TRACK
            .build() // Khởi tạo trình phát
        // Quản lý âm thanh
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume,0)
        simpleExoPlayer.addListener(playerlistener) // Lắng nghe
        exoPlayerView.player = simpleExoPlayer // gắng trình phát vào videoView
        binding.youtubeOverlay.player(simpleExoPlayer) // gắng API duable tap

        seekBarFeature()

        //TODO tạo nguồn truyền thông
        createMediaSource()

//        simpleExoPlayer.setMediaSource(mediaSource)
//        simpleExoPlayer.prepare()


        //TODO BUTTER AUDIO
        loudnessEnhancer = LoudnessEnhancer(simpleExoPlayer.audioSessionId)
        loudnessEnhancer.setTargetGain(0)

        //DEFAULT SPEED
        speed = 1f

        initializeBinding()
        setVisibility()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeBinding() {
        binding.exoPlayerView.setOnTouchListener { _, motionEvent ->
            gestureDetectorCompat.onTouchEvent(motionEvent)
            if (motionEvent.action == MotionEvent.ACTION_UP){
                binding.brightnessIcon.visibility = View.GONE
                binding.volumeIcon.visibility = View.GONE
            }
            return@setOnTouchListener false
        }
        // exoplayer player viodeo streaming m3u8

//        TODO Chạm 2 lần để tua hoặc lùi
        binding.youtubeOverlay.performListener(object : YouTubeOverlay.PerformListener {
            override fun onAnimationEnd() {
                binding.youtubeOverlay.visibility = View.GONE
            }

            override fun onAnimationStart() {
                binding.youtubeOverlay.visibility = View.VISIBLE
            }
        })

        //TODO nút quay lại
        binding.backBtn.setOnClickListener {
            finish()
        }

        //TODO Dừng hoặc phát video
        binding.playPauseBtn.setOnClickListener {
            if(simpleExoPlayer.isPlaying){
                pauseVideo()
            }else{
                playVideo()
            }
        }

        //TODO bật tắt lặp lại
        binding.repeatBtn.setOnClickListener {
            if (repeat) {
                repeat = false
                simpleExoPlayer.repeatMode = Player.REPEAT_MODE_OFF
                binding.repeatBtn.setImageResource(com.google.android.exoplayer2.R.drawable.exo_controls_repeat_off)

            } else {
                repeat = true
                simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                binding.repeatBtn.setImageResource(com.google.android.exoplayer2.R.drawable.exo_controls_repeat_all)
            }
        }

        binding.lockIcon.setOnClickListener {
            if (!isLocked){
                isLocked = true
                binding.exoPlayerView.useController = false
                binding.exoPlayerView.hideController()
                binding.lockIcon.setImageResource(R.drawable.ic_baseline_lock_24)
            }else{
                isLocked = false
                binding.exoPlayerView.useController = true
                binding.exoPlayerView.showController()
                binding.lockIcon.setImageResource(R.drawable.ic_baseline_lock_open_24)
            }
        }

        //TODO NÚT TÍNH NĂNG
        binding.moreFeaturesBtn.setOnClickListener {
            pauseVideo()

            val customDialog = LayoutInflater.from(this).inflate(R.layout.more_features,binding.root, false)
            val bindingMF = MoreFeaturesBinding.bind(customDialog)
            val dialog = MaterialAlertDialogBuilder(this).setView(customDialog)
                .setOnCancelListener {
                    playVideo()
                }
                .setBackground(ColorDrawable(0x00000000.toInt()))
                .create()
            dialog.show()


            //TODO TÍNH NĂNG AUDIO
            bindingMF.audioTrack.setOnClickListener {
                dialog.dismiss()
                playVideo()

                val audioTrack = ArrayList<String>()

                for (i in 0 until simpleExoPlayer.currentTrackGroups.length){
                    if(simpleExoPlayer.currentTrackGroups.get(i).getFormat(0).selectionFlags == C.SELECTION_FLAG_DEFAULT){
                        audioTrack.add(Locale(simpleExoPlayer.currentTrackGroups.get(i).getFormat(0).language.toString()).displayCountry)
                    }
                }

                val tempTracks = audioTrack.toArray(arrayOfNulls<CharSequence>(audioTrack.size))
                MaterialAlertDialogBuilder(this).setTitle("AUDIO TRACK")
                    .setOnCancelListener {
                        playVideo()
                    }
                    .setBackground(ColorDrawable(0x80000000.toInt()))
                    .setItems(tempTracks){_,position ->
                        Toast.makeText(this, audioTrack[1] + "selected", Toast.LENGTH_SHORT).show()
                        trackSelector.setParameters(trackSelector.buildUponParameters()
                            .setPreferredAudioLanguage(audioTrack[position]))

                    }
                    .create()
                    .show()
            }

            //TODO TÍNH NĂNG SUBTITLE
            bindingMF.subtitleBtn.setOnClickListener {
                if (isSubtitle){
                    trackSelector.parameters = DefaultTrackSelector.ParametersBuilder(this).setRendererDisabled(
                        C.TRACK_TYPE_VIDEO, true
                    ).build()
                    Toast.makeText(this, "Subtitle on", Toast.LENGTH_SHORT).show()
                    isSubtitle = false
                }else{
                    trackSelector.parameters = DefaultTrackSelector.ParametersBuilder(this).setRendererDisabled(
                        C.TRACK_TYPE_VIDEO, false
                    ).build()
                    Toast.makeText(this, "Subtitle off", Toast.LENGTH_SHORT).show()
                    isSubtitle = true
                }
            }


            //TODO audioBooster
            bindingMF.audioBooster.setOnClickListener {
                dialog.dismiss()
                val customdialogB = LayoutInflater.from(this).inflate(R.layout.booster, binding.root, false)
                val bindingB = BoosterBinding.bind(customdialogB)
                val dialogB = MaterialAlertDialogBuilder(this).setView(customdialogB)
                    .setOnCancelListener { playVideo() }
                    .setPositiveButton("OKE"){self ,_ ->
                        loudnessEnhancer.setTargetGain(bindingB.seekbar.progress*100)
                        self.dismiss()
                    }
                    .setBackground(ColorDrawable(0x80000000.toInt()))
                    .create()
                dialogB.show()
                bindingB.seekbar.progress=loudnessEnhancer.targetGain.toInt() / 100
                bindingB.tv.text = "Audio booster \n\n ${loudnessEnhancer.targetGain.toInt()/10}"
                bindingB.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        bindingB.tv.text = "Audio booster \n\n${p1*10   }"
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }

                })
                playVideo()
            }

            //TODO CHANGE SPEED
            bindingMF.speedBtn.setOnClickListener {
                dialog.dismiss()
                val customdialogS = LayoutInflater.from(this).inflate(R.layout.speed_dialog, binding.root, false)
                val bindingS = SpeedDialogBinding.bind(customdialogS)
                val dialogS = MaterialAlertDialogBuilder(this).setView(customdialogS)
                    .setOnCancelListener { playVideo() }
                    .setPositiveButton("OKE"){self ,_ ->
                        self.dismiss()
                    }
                    .setBackground(ColorDrawable(0x80000000.toInt()))
                    .create()
                dialogS.show()
                playVideo()

                bindingS.addBtn.setOnClickListener {
                    changeSpeed(true)
                    bindingS.tvSpeed.text = "${DecimalFormat("#.##").format(speed)}X"
                }

                bindingS.minusBtn.setOnClickListener {
                    changeSpeed(false)
                    bindingS.tvSpeed.text = "${DecimalFormat("#.##").format(speed)}X"
                }
            }


            //TODO SLEEP TIME
            bindingMF.sleepTime.setOnClickListener {
                dialog.dismiss()
                if(timer != null) {
                    Toast.makeText(this, "Bộ hẹn giờ đã bật, để tắt vui lòng reset app", Toast.LENGTH_LONG).show()
                }else{

                    var sleepTime = 15
                    val customdialogS = LayoutInflater.from(this).inflate(R.layout.speed_dialog, binding.root, false)
                    val bindingS = SpeedDialogBinding.bind(customdialogS)
                    val dialogS = MaterialAlertDialogBuilder(this).setView(customdialogS)
                        .setOnCancelListener { playVideo() }
                        .setPositiveButton("OKE"){self ,_ ->
                            timer = Timer()
                            val task = object : TimerTask(){
                                override fun run() {
                                    moveTaskToBack(true)
                                    exitProcess(1)
                                }

                            }
                            timer!!.schedule(task,sleepTime*60*1000.toLong())
                            self.dismiss()
                        }
                        .setBackground(ColorDrawable(0x80000000.toInt()))
                        .create()
                    dialogS.show()
                    playVideo()

                    bindingS.tvSpeed.text = "$sleepTime Min"
                    bindingS.addBtn.setOnClickListener {
                        if(sleepTime < 120) sleepTime+=15
                        bindingS.tvSpeed.text = "$sleepTime Min"
                    }

                    bindingS.minusBtn.setOnClickListener {
                        if(sleepTime > 1)   sleepTime-=1
                        bindingS.tvSpeed.text = "$sleepTime Min"
                    }
                }
            }


            //TODO PIP
            bindingMF.pipBtn.setOnClickListener {
                Toast.makeText(this, "TÍNH NĂNG ĐANG PHÁT TRIỂN", Toast.LENGTH_SHORT).show()
            }

        }




    }

    //TODO loadding và chạy video
    private fun createMediaSource(){
        urlType = URLType.HLS
        urlType.url = "https://ali-cdn-play.loklok.tv/434db5fef78e46b99ac5838f57f53c01/6127608444d640a4be5107040d828fb1-371953b1bd92975aadc6df98fbf40fc9-sd.m3u8?auth_key=1648981746-97c79c282c524f23a2d1a64b20aba2dd-0-b6562b5080e9134b001fc8b82a5e8a2e"


//        urlType= URLType.MP4
//        urlType.url ="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"



        when(urlType){
            URLType.MP4 -> {
                val dataSourceFactory : DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, applicationInfo.name)
                )

                mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlMedia))
                )
            }




            URLType.HLS -> {

                val dataSourceFactory : DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, applicationInfo.name)
                )
                mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlMedia))
                )

                val assetSrtUri = Uri.parse(subUrl)
                val assetVideoUri = Uri.parse((urlMedia))

                val subtitle = MediaItem.SubtitleConfiguration.Builder(assetSrtUri)
                    .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                    .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                    .build()

                val mediaItem = MediaItem.Builder()
                    .setUri(assetVideoUri)
                    .setSubtitleConfigurations(ImmutableList.of(subtitle))
                    .build()


                simpleExoPlayer.setMediaItem(mediaItem)
                simpleExoPlayer.prepare()
                simpleExoPlayer.play()

            }
        }
    }

    //TODO
    private fun seekBarFeature(){
        findViewById<DefaultTimeBar>(com.google.android.exoplayer2.R.id.exo_progress).addListener(object : TimeBar.OnScrubListener{
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                pauseVideo()
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                Glide.with(this@HlsActivity)
                    .load("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAeFBMVEX////lCRT+7u/tTlb1m6Dxdnz6z9HtU1v3r7P0k5jzi5DoIizsRE32qKz4tLjsSFD5vsH2o6f5x8n+9vbrPUXmEBvnGyXvZGvqMjv+8/TuWWD95ObqNj/3rbDyfIL829zygojpKDLvX2fwbHL709X4ur3oHyn96Olm4M4yAAADiklEQVR4nO2Z23aqMBQAwTtSFAEVsV6p9f//8CgJkCh2EU7b9GHmRXYgkgFCdoLjAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwF/HLViX4ZuIRTBwn5jdiifPxa47ve0YPRfPb8U9sTl+PLUsHxbBVARpuVOeexWbiSzL462JXJciiuTOVN3ZXsQNbIs4YxHlIgpl/YOpSN+6yExEy00RZXqz2ossr7ZFHF+Eg/v2VjmdmYjrWRc5ijBRtk/tPSqR5LXIe7/m3hcjsfku9qYi2tUiqXL8vr3IQbbkw3Hiz/rqmIrIbtUkEjTXnGi30qlFHk/fUsSZV3Egtpa9LiJH6yI7Ed+GDtmK9WONViIn6yLxSp6v7GujTiJiKLIp4gxFwTwXv4mJhyKSWxf5kC2Rg/yio0iR1lgVcRJXoX2a9SBSjBF2RRaqyN7IQxXJrItslkpztp1F7q/tJpFjFIaX2SQI9AzupchJHQ2NRJy8bs3T4e1F7r3ryxRF730vRSr6xiJRXfnyHyJz6yLlNMQszVJE5LO5tS7ilXW9x6PbifjixTe1LtKTl9QozVJFxJVIrYs4n1pVcxE5kxnZFpF5bzlRNBeRt2LYJHL2BotFcEN/s//AOKKM7YOuIqLRvt0BUXn9vnUVucpbalckq5/L9usnuojTdxUsiWyVJhimWrWIlrBZEhkqTfC7ivTUhM2OyGalXkzDHKXWH1sX8VyVrKtIoPyJHZGTKJDrTIaDey2iTgasiJQLf1Hy9M9GIuq7r1Fkcz2MlKtkKuLnw+PUW0zC6LBtFJFjWFItDHYV2b0QGefZ/P0kOqKyGGsqojJqECkTnKB673RYDipE4vrZ0kRUflDkLMLPuNps/3FEFynr2xHZyst4dKqvI0brKKrIzKbIXkTi+8ZJBLuOInE1IP2+SHlusd4rvyiazEpUEWdtIhKNx9n5vA7rkk1Y8LiOE8/f3lN/pWYOzyIDdU+VdBksCWkilxciSz+dZ/nRC0LjGahm1DuMwsnCOw63TyJy1aHM3sfNN7etiLPSRMJ87y1m0cF4ttaS27h0Cbwipyo/fk7kLvncpl/UfkAXyV0/yfYD0zWl72Ab7aZ5nV/JuXv0RQ0dXSQ2Wzj+QaYrpe+3YZWsp0FkuM76K1zDwbCf/pkrCwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA8O38A65cMWzzCFSrAAAAAElFTkSuQmCC")
                    .placeholder(R.drawable.netflix)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(findViewById<ImageView>(R.id.imageViewFrame))



                simpleExoPlayer.seekTo(position)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                playVideo()
            }

        })
    }


    //TODO Lắng nghe trình phát
    private var playerlistener = object : Player.Listener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)

            when(playbackState) {
                ExoPlayer.STATE_BUFFERING -> {binding.progressBar.visibility = View.VISIBLE}
                ExoPlayer.STATE_READY -> {binding.progressBar.visibility = View.GONE}
            }
        }

        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()

            if (urlType == URLType.HLS) {
                exoPlayerView.useController = true
            }

            if (urlType == URLType.MP4) {
                exoPlayerView.useController = true
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(this@HlsActivity, "${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    //TODO theo dõi trình player có show hay ko show
    private fun setVisibility(){
        runnable = Runnable{
            if(binding.exoPlayerView.isControllerVisible){
                changeVisibility(View.VISIBLE)
            }else{
                changeVisibility(View.GONE)
            }
            Handler(Looper.getMainLooper()).postDelayed(runnable,0)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)
    }
    private fun changeVisibility(visibility : Int){
        binding.topController.visibility = visibility
        binding.bottomController.visibility = visibility
        binding.playPauseBtn.visibility = visibility
        if (isLocked){
            binding.lockIcon.visibility = View.VISIBLE
        }else{
            binding.lockIcon.visibility = visibility
        }


    }

    //TODO CHANGE SPEED
    private fun changeSpeed(isIncrement: Boolean){
        if(isIncrement){
            if(speed <= 2.9f){
                speed+=0.10f
            }
        }else{
            if(speed > 0.20f){
                speed-=0.10f
            }
        }
        simpleExoPlayer.setPlaybackSpeed(speed)
    }

    private fun playVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_24)
        simpleExoPlayer.play()
    }

    private fun pauseVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        simpleExoPlayer.pause()
    }


    //TODO ẩn hiện ui
    private fun hideSystemUI(){
        actionBar?.hide()
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }
    private fun showSystemUi(){
        actionBar?.show()
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    //TODO nút quay lại để huỷ activuty
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    //TODO set lại độ sáng màng hình
    private fun setScreenBrightness(value : Int){
        val d = 1.0f/30
        val lp = this.window.attributes
        lp.screenBrightness = d * value
        this.window.attributes = lp
    }




    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val constraintSet = ConstraintSet()
        constraintSet.connect(exoPlayerView.id,
            ConstraintSet.TOP, ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,0)
        constraintSet.connect(exoPlayerView.id,
            ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,0)
        constraintSet.connect(exoPlayerView.id,
            ConstraintSet.START, ConstraintSet.PARENT_ID,
            ConstraintSet.START,0)
        constraintSet.connect(exoPlayerView.id,
            ConstraintSet.END, ConstraintSet.PARENT_ID,
            ConstraintSet.END,0)

        constraintSet.applyTo(constraintLayoutRoot)


        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideSystemUI()
        }else{
            showSystemUi()
            val layoutParams = exoPlayerView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.dimensionRatio = "16:9"
        }

        window.decorView.requestLayout()
    }

    //TODO Vòng Đời Trình phát
    override fun onResume() {
        super.onResume()

        simpleExoPlayer.playWhenReady = true

        simpleExoPlayer.play()
    }

    override fun onPause() {
        super.onPause()

        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()

        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false

    }

    override fun onDestroy() {
        super.onDestroy()

        simpleExoPlayer.removeListener(playerlistener)
        simpleExoPlayer.stop()
        simpleExoPlayer.clearMediaItems()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
        return Unit
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }



    override fun onLongPress(p0: MotionEvent?) {
        return Unit
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onScroll(e: MotionEvent?, e1: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        val sWidth = Resources.getSystem().displayMetrics.widthPixels

        if(abs(distanceX) < abs(distanceY)){
            if(e!!.x < sWidth/2){
                binding.brightnessIcon.visibility = View.VISIBLE
                binding.volumeIcon.visibility = View.GONE

                val increase = distanceY > 0
                val newValue = if (increase) brightness + 1 else brightness - 1
                binding.brightnessIcon.text = brightness.toString()
                if (newValue in 0..30){
                    brightness = newValue


                }

                setScreenBrightness(brightness)
            }else{
                binding.volumeIcon.text = e!!.x.toString()
                binding.brightnessIcon.visibility = View.GONE
                binding.volumeIcon.visibility = View.VISIBLE

                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

                val increase = distanceY > 0
                val newValue = if (increase) volume + 1 else volume - 1
                binding.volumeIcon.text = (volume*10).toString() + "%"
                if (newValue in 0..maxVolume){
                    volume = newValue
                }

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
            }
        }

        return true
    }


}

enum class URLType(var url : String){
    MP4(""),HLS("")
}