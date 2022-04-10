package com.example.dacs3.data.retrofit.module.getDetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Image(

    @SerializedName("bannerImage")@Expose var bannerImage: String,
    @SerializedName("mainImage")@Expose var mainImage: String

)