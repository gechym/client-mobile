package com.example.dacs3.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3.R
import com.example.dacs3.Util.const
import com.example.dacs3.Util.const.HOST_LINK
import com.example.dacs3.data.retrofit.ApiRetrofit
import com.example.dacs3.data.retrofit.module.getDetail.EpisodeDetails
import com.example.dacs3.databinding.ItemEpisodeBinding
import com.example.dacs3.databinding.LayortLoadingDialogBinding
import com.example.dacs3.databinding.LayoutErrorDialogBinding
import com.example.dacs3.ui.activity.DetailActivity
import com.example.dacs3.ui.activity.HlsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EpisodeAdapter(
    private val listEpivesode: ArrayList<EpisodeDetails>,
    private val id: Int,
    private val category: Int,
    private val contextParent: Context
) : RecyclerView.Adapter<EpisodeAdapter.viewHolder>() {

    lateinit var gson: Gson
    lateinit var proviceRetrofit: Retrofit.Builder
    lateinit var proviceMovieService: ApiRetrofit

    val successDialog = LayoutInflater.from(contextParent)
        .inflate(R.layout.layort_loading_dialog,null , false)
    val dialog = MaterialAlertDialogBuilder(contextParent).setView(successDialog)
        .setBackground(
            ColorDrawable(0x00000000.toInt())
        ).create()


    inner class viewHolder(binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {
        val btn = binding.button
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeAdapter.viewHolder {
        val layout = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(layout)
    }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("chekLog", "Lỗi tại $throwable")
        Toast.makeText(contextParent, "${throwable.toString()}", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

    override fun onBindViewHolder(holder: EpisodeAdapter.viewHolder, position: Int) {
        val item = listEpivesode[position]
        if (listEpivesode.size == 1){
            holder.btn.text = "Xem Phim online"
            holder.btn.setOnClickListener {


                gson = GsonBuilder().create()
                proviceRetrofit = Retrofit.Builder()
                    .baseUrl(HOST_LINK)
                    .addConverterFactory(GsonConverterFactory.create(gson))

                proviceMovieService = proviceRetrofit.build().create(ApiRetrofit::class.java)

//                val successDialog = LayoutInflater.from(contextParent)
//                    .inflate(R.layout.layort_loading_dialog,null , false)
//                val dialog = MaterialAlertDialogBuilder(contextParent).setView(successDialog)
//                    .setBackground(
//                        ColorDrawable(0x00000000.toInt())
//                    ).create()
                dialog.show()


                val scopeMain = CoroutineScope(Dispatchers.Main + exceptionHandler)
                scopeMain.launch {
                    try {
                        Log.e(
                            "CheckLog",
                            "${item.seriesNo} ${item.resolution} ${item.resolution[0].description} ${id} ${category}"
                        )

                        val urlMedia = proviceMovieService.getMediaMovie(
                            category = category,
                            contentId = id,
                            episodeId = item.episodeId,
                            definition = item.resolution[0].code
                        ).body()?.data?.mediaUrl
                        Log.e(
                            "CheckLog", urlMedia.toString()
                        )


                        val intent = Intent(contextParent, HlsActivity::class.java)
                        intent.putExtra("urlMedia", urlMedia)
                        ContextCompat.startActivity(contextParent, intent, null)

                        dialog.dismiss()
                    }catch (e : Exception) {
                        dialog.dismiss()
                        Toast.makeText(contextParent, "${e.toString()}", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
        else{
            holder.btn.append(" "+item.seriesNo.toString())
            holder.btn.setOnClickListener {
                gson = GsonBuilder().create()

                proviceRetrofit = Retrofit.Builder()
                    .baseUrl(HOST_LINK)
                    .addConverterFactory(GsonConverterFactory.create(gson))

                proviceMovieService = proviceRetrofit.build().create(ApiRetrofit::class.java)

                val successDialog = LayoutInflater.from(contextParent)
                    .inflate(R.layout.layort_loading_dialog,null , false)
                val dialog = MaterialAlertDialogBuilder(contextParent).setView(successDialog)
                    .setBackground(
                        ColorDrawable(0x00000000.toInt())
                    ).create()
                dialog.show()

                GlobalScope.launch {
                    Log.e(
                        "CheckLog",
                        "${item.seriesNo} ${item.resolution[0].code} ${item.resolution[0].description} ${id} ${category}"
                    )

                    val urlMedia = proviceMovieService.getMediaMovie(
                        category = category,
                        contentId = id,
                        episodeId = item.episodeId,
                        definition = item.resolution[0].code
                    ).body()?.data?.mediaUrl
                    Log.e(
                        "CheckLog", urlMedia.toString()
                    )

//                id = intent.getIntExtra("id",12228)
//                category = intent.getIntExtra("category",1)
                    val intent = Intent(contextParent, HlsActivity::class.java)
                    intent.putExtra("urlMedia", urlMedia)
                    ContextCompat.startActivity(contextParent, intent, null)

                    dialog.dismiss()
                }

            }
        }




    }

    override fun getItemCount(): Int {
        if (listEpivesode.size == 1) {
            return 1
        }
        return listEpivesode.size
    }
}