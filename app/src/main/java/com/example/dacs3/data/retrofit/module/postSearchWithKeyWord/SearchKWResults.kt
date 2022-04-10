package com.example.example

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SearchKWResults(

    @SerializedName("areas") @Expose var areas: ArrayList<Areas>,
    @SerializedName("categoryTag") @Expose var categoryTag: ArrayList<CategoryTag>,
    @SerializedName("coverHorizontalUrl") @Expose var coverHorizontalUrl: String,
    @SerializedName("coverVerticalUrl") @Expose var coverVerticalUrl: String,
    @SerializedName("domainType") @Expose var domainType: Int,
    @SerializedName("dramaType") @Expose var dramaType: DramaType,
    @SerializedName("duration") @Expose var duration: String,
    @SerializedName("id") @Expose var id: String,
    @SerializedName("name") @Expose var name: String,
    @SerializedName("releaseTime") @Expose var releaseTime: String,
    @SerializedName("sort") @Expose var sort: String,
    @SerializedName("upInfo") @Expose var upInfo: UpInfo

)