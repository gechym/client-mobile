package com.example.dacs3.data.retrofit.module.getDetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Resolution(

    @SerializedName("code")@Expose var code: String,
    @SerializedName("description")@Expose var description: String

)