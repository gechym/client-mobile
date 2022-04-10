package com.example.dacs3.data.retrofit.module.getMediaData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data (

    @SerializedName("mediaUrl"      ) @Expose var mediaUrl      : String? = null,
    @SerializedName("totalDuration" ) @Expose var totalDuration : Int?    = null,
    @SerializedName("episodeId"     ) @Expose var episodeId     : String? = null

)