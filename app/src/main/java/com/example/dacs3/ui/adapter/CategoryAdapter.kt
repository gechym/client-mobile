package com.example.dacs3.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3.R
import com.example.dacs3.databinding.ItemCategoryBinding
import com.example.dacs3.databinding.ItemLoadingBinding
import com.example.dacs3.databinding.ItemSliderBinding
import com.example.dacs3.module.CategoryFilm
import kotlin.properties.Delegates

class CategoryAdapter(private var list: MutableList<CategoryFilm>, private val context: Context) :
    RecyclerView.Adapter<CategoryAdapter.viewHolder>() {

    companion object {
        const val TYPR_ITEM = 1
        const val TYPE_LOADING = 2
    }

    override fun getItemViewType(position: Int): Int {
        if(position == list.size - 1){
            return TYPE_LOADING
        }


        return TYPR_ITEM
    }

    inner class viewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameCategory = binding.nameCategory
        val recyclerView = binding.recyclerview
    }


    inner class viewHodlderLoading(view : View) : RecyclerView.ViewHolder(view) {
        val progressBar :ProgressBar = view.findViewById(R.id.progressBar)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

            val layout = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return viewHolder(layout)


    }


    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val categoryFilm = list[position]
        holder.nameCategory.text = categoryFilm.title
        holder.recyclerView.setHasFixedSize(true)


        holder.recyclerView.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL, false)
        holder.recyclerView.adapter = FlilmAdapter(categoryFilm.listFilm,categoryFilm.mainSlider,context)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addListMore(list: MutableList<CategoryFilm>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.list = mutableListOf()
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int {
        return list.size
    }


}