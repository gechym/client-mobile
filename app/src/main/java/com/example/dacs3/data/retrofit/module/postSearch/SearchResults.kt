package com.example.dacs3.data.retrofit.module.postSearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchResults(

    @SerializedName("coverVerticalUrl") @Expose var coverVerticalUrl: String,
    @SerializedName("domainType") @Expose var domainType: Int,
    @SerializedName("id") @Expose var id: String,
    @SerializedName("name") @Expose var name: String,
    @SerializedName("sort") @Expose var sort: String

)