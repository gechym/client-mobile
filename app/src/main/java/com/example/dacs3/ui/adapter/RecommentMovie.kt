package com.example.dacs3.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dacs3.R
import com.example.dacs3.data.retrofit.module.getDetail.RecommendMovies
import com.example.dacs3.databinding.BottomSheetDialogBinding
import com.example.dacs3.databinding.ItemFilmBinding
import com.example.dacs3.ui.activity.DetailActivity
import com.google.android.material.bottomsheet.BottomSheetDialog


class RecommentMovieAdapter(
    private val list: List<RecommendMovies>,
    private val context: Context,
    private val root: ConstraintLayout
) : RecyclerView.Adapter<RecommentMovieAdapter.viewHolder>() {
    lateinit var bottomSheetDialog: BottomSheetDialog

    inner class viewHolder(binding: ItemFilmBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.img
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layout = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(layout)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val film = list[position]



        Glide.with(context)
            .load(film.image.mainImage)
            .placeholder(R.drawable.animation_loading)
            .error(R.drawable.animation_loading)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .into(holder.img)

        holder.img.setOnClickListener {
            bottomSheetDialog = BottomSheetDialog(context)
            val bottomSheetDialogLayout = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_dialog,root , false)
            bottomSheetDialog.setContentView(bottomSheetDialogLayout)
            bottomSheetDialog.show()


            val bindingBottomSheetDialog = BottomSheetDialogBinding.bind(bottomSheetDialogLayout)

            bindingBottomSheetDialog.apply {
                this.btnPlay.setOnClickListener {


                    bottomSheetDialog.dismiss()


                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("id", film.movieId.toInt())
                    intent.putExtra("category", film.categoryId)

                    Toast.makeText(context, "${film.movieId} ${film.categoryId}", Toast.LENGTH_SHORT).show()

                    ContextCompat.startActivity(context, intent, null)
                }

                Glide.with(context)
                    .load(film.image.mainImage)
                    .placeholder(R.drawable.animation_loading)
                    .error(R.drawable.img_2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .into(this.image)

                this.title.text = film.name
                this.description.text = ""
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}