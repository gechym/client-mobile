package com.example.dacs3.data.retrofit.module.getDetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailMovie(

    @SerializedName("status")@Expose var status: Int,
    @SerializedName("data")@Expose var data: Data,
)