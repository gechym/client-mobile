package com.example.dacs3.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dacs3.R
import com.example.dacs3.Util.const.HOST_LINK
import com.example.dacs3.data.retrofit.ApiRetrofit
import com.example.dacs3.data.retrofit.module.getDetail.Data
import com.example.dacs3.databinding.ActivityDetailBinding
import com.example.dacs3.ui.adapter.EpisodeAdapter
import com.example.dacs3.ui.adapter.RecommentMovieAdapter
import com.example.dacs3.ui.adapter.RelatedSeriesAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates


class DetailActivity : YouTubeBaseActivity() {

    lateinit var binding: ActivityDetailBinding

    lateinit var gson: Gson
    lateinit var proviceRetrofit: Retrofit.Builder
    lateinit var proviceMovieService: ApiRetrofit


    companion object {
        const val API_KEY = "AIzaSyAMLhicKHJG0-jAnwlRaAkFrrZqxTMIeg4"
        lateinit var key: String

        @SuppressLint("StaticFieldLeak")
        lateinit var youTubePlayer: YouTubePlayerView
        lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener

        var id by Delegates.notNull<Int>()
        var category by Delegates.notNull<Int>()
    }


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Hide system bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        showUi(false)

        id = intent.getIntExtra("id", 12228)
        category = intent.getIntExtra("category", 1)

        Toast.makeText(this, "$id $category", Toast.LENGTH_SHORT).show()

        val scopeMain = CoroutineScope(Dispatchers.Main)
        scopeMain.launch {
            getDataDetailMovie(id, category)
        }


    }

    private fun setInit() {

        // Set tablayout
//        val viewPagerAdapter = ViewPagerAdapter(
//            supportFragmentManager
//        )
//        binding.viewPager.adapter = viewPagerAdapter
//        binding.tableLayout.setupWithViewPager(binding.viewPager)
//        binding.tableLayout.tag = 1

        if (key == "0") {
            binding.trailerYTB.visibility = View.GONE
        } else {
            binding.trailerYTB.visibility = View.VISIBLE

            youTubePlayer = binding.trailerYTB
            youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubePlayer?,
                    p2: Boolean
                ) {
                    p1?.loadVideo(key)
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    Log.e("CheckLog", "failed YTB")
                }

            }

            youTubePlayer.initialize(API_KEY, youtubePlayerInit)
        }




        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    suspend fun getDataDetailMovie(id: Int, category: Int) {
        try {
            gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()


            proviceRetrofit = Retrofit.Builder()
                .baseUrl(HOST_LINK)
                .addConverterFactory(GsonConverterFactory.create(gson))

            proviceMovieService = proviceRetrofit.build().create(ApiRetrofit::class.java)


            val res = proviceMovieService.getDetailMovie(id, category)

            try {
                if (res.isSuccessful) {
                    Log.e("CheckLog", "${res.code()} : ${res.body()}")
                    key = res.body()?.data?.trailer ?: "0"
                    renderData(res.body()?.data)
                    showUi(true)
                } else {
                    Log.e("CheckLog", "${res.code()} : ${res.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("CheckLog", e.toString())
            }
        } catch (e: Exception) {
            Log.e("CheckLog 1", e.toString())
        }


    }

    private fun renderData(data: Data?) {
        if (data != null) {
            binding.title.text = data.name
            binding.description.text = data.introduction
            binding.imdb.text = data.score.toString()
            if (key == "0") {
                binding.trailerYTB.visibility = View.GONE
            }


            Glide.with(this)
                .load(data.image.mainImage)
                .placeholder(R.drawable.animation_loading)
                .error(R.drawable.img_2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(binding.poter)

            Glide.with(this)
                .load(data.image.bannerImage)
                .placeholder(R.drawable.animation_loading)
                .error(R.drawable.img_2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(binding.banner)

            if (data.episodeDetails.size == 1) { // Phim điện ảnh
                val successDialog = LayoutInflater.from(this)
                    .inflate(R.layout.layort_loading_dialog, null, false)
                val dialog = MaterialAlertDialogBuilder(this).setView(successDialog)
                    .setBackground(
                        ColorDrawable(0x00000000.toInt())
                    ).create()

                binding.play.setOnClickListener {
                    dialog.show()
                    val scopeMain = CoroutineScope(Dispatchers.Main)
                    scopeMain.launch {
                        try {
                            val urlMedia = proviceMovieService.getMediaMovie(
                                category = category,
                                contentId = id,
                                episodeId = data.episodeDetails[0].episodeId,
                                definition = data.episodeDetails[0].resolution[0].code
                            ).body()?.data?.mediaUrl

                            Log.e(
                                "CheckLog", urlMedia.toString()
                            )
                            dialog.dismiss()
                            val intent = Intent(this@DetailActivity, HlsActivity::class.java)
                            intent.putExtra("urlMedia", urlMedia)
                            ContextCompat.startActivity(this@DetailActivity, intent, null)
                        } catch (e: Exception) {

                            dialog.dismiss()
                            Toast.makeText(
                                this@DetailActivity,
                                "Film đang được vui lòng gửi thử lại...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

            } else {
                binding.recyclerviewEpisode.setHasFixedSize(true)
                binding.recyclerviewEpisode.layoutManager = GridLayoutManager(this, 4)
                binding.recyclerviewEpisode.adapter =
                    EpisodeAdapter(data.episodeDetails, id, category, this)
            }








            binding.recyclerviewRecomment.setHasFixedSize(true)
            binding.recyclerviewRecomment.layoutManager = GridLayoutManager(this, 3)
            binding.recyclerviewRecomment.adapter =
                RecommentMovieAdapter(data.recommendMovies, this, binding.root)


            binding.recyclerviewSeries.setHasFixedSize(true)
            binding.recyclerviewSeries.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.HORIZONTAL, false
            )
            binding.recyclerviewSeries.adapter =
                RelatedSeriesAdapter(data.relatedSeries, this, binding.root)
        }
    }


    private fun showUi(isShow: Boolean) {
        if (isShow) {

            binding.banner.visibility = View.VISIBLE
            binding.constraintLayout.visibility = View.VISIBLE
            binding.trailerYTB.visibility = View.VISIBLE
            binding.description.visibility = View.VISIBLE
            binding.recyclerviewEpisode.visibility = View.VISIBLE
            binding.textViewRecomment.visibility = View.VISIBLE
            binding.recyclerviewRecomment.visibility = View.VISIBLE


            binding.animationView.visibility = View.GONE

            setInit()

        } else {
            binding.banner.visibility = View.GONE
            binding.constraintLayout.visibility = View.GONE
            binding.trailerYTB.visibility = View.GONE
            binding.description.visibility = View.GONE
            binding.recyclerviewEpisode.visibility = View.GONE
            binding.textViewRecomment.visibility = View.GONE
            binding.recyclerviewRecomment.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

}