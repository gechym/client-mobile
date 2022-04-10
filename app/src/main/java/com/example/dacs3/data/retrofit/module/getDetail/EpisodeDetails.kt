package com.example.dacs3.data.retrofit.module.getDetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EpisodeDetails(

    @SerializedName("episodeId")@Expose var episodeId: Int,
    @SerializedName("seriesNo")@Expose var seriesNo: Int,
    @SerializedName("resolution")@Expose var resolution: ArrayList<Resolution>,
    @SerializedName("subtitles")@Expose var subtitles: ArrayList<Subtitles>

)