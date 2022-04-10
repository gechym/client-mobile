package com.example.dacs3.data.retrofit.module.getHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListMovie(
    @SerializedName("homeSectionName")
    @Expose
    val homeSectionName: String,

    @SerializedName("listMovie")
    @Expose
    val listMovie: List<MovieNetworkEntity>
)