package com.example.example

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UpInfo(

    @SerializedName("enable") @Expose var enable: Boolean,
    @SerializedName("upId") @Expose var upId: Int,
    @SerializedName("upImgUrl") @Expose var upImgUrl: String,
    @SerializedName("upName") @Expose var upName: String

)