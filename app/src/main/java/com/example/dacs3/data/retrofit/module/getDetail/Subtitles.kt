package com.example.dacs3.data.retrofit.module.getDetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Subtitles(

    @SerializedName("language")@Expose var language: String,
    @SerializedName("languageAbbr")@Expose var languageAbbr: String,
    @SerializedName("subtitlingUrl")@Expose var subtitlingUrl: String

)