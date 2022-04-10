package com.example.dacs3.data.retrofit.module.getHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListCategory(
    @SerializedName("data")
    @Expose
    val data : List<ListMovie>
)


