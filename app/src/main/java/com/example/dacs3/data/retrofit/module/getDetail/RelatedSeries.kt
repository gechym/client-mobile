package com.example.dacs3.data.retrofit.module.getDetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RelatedSeries(

    @SerializedName("categoryId")@Expose var categoryId: Int,
    @SerializedName("movieId")@Expose var movieId: Int,
    @SerializedName("name")@Expose var name: String,
    @SerializedName("seriesNo")@Expose var seriesNo: Int,
    @SerializedName("image")@Expose var image: Image

)