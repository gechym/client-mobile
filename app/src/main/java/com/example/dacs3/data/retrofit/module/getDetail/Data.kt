package com.example.dacs3.data.retrofit.module.getDetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("movieId") @Expose var movieId: Int,
    @SerializedName("categoryId")@Expose var categoryId: Int,
    @SerializedName("releaseYear")@Expose var releaseYear: Int,
    @SerializedName("name")@Expose var name: String,
    @SerializedName("category")@Expose var category: ArrayList<String>,
    @SerializedName("episodeCount")@Expose var episodeCount: Int,
    @SerializedName("nation")@Expose var nation: String,
    @SerializedName("score")@Expose var score: Double,
    @SerializedName("image")@Expose var image: Image,
    @SerializedName("introduction")@Expose var introduction: String,
    @SerializedName("episodeDetails")@Expose var episodeDetails: ArrayList<EpisodeDetails>,
    @SerializedName("recommendMovies")@Expose var recommendMovies: ArrayList<RecommendMovies>,
    @SerializedName("relatedSeries")@Expose var relatedSeries: ArrayList<RelatedSeries>,
    @SerializedName("trailer")@Expose val trailer: String
)