package com.example.dacs3.data.retrofit.module.getHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieNetworkEntity(

    @SerializedName("id")
    @Expose
    val id : Int,

    @SerializedName("category")
    @Expose
    val category: Int,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("contentType")
    @Expose
    val contentType : String,

    @SerializedName("imageUrl")
    @Expose
    val imageUrl : String

)