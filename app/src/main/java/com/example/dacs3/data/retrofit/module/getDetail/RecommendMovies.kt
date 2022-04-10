package com.example.dacs3.data.retrofit.module.getDetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RecommendMovies(

    @SerializedName("categoryId")@Expose var categoryId: Int,
    @SerializedName("movieId")@Expose var movieId: String,
    @SerializedName("name")@Expose var name: String,
    @SerializedName("image")@Expose var image: Image

)