package com.example.example

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Data(

    @SerializedName("searchResults") @Expose var searchResults: ArrayList<SearchKWResults>,
    @SerializedName("searchType") @Expose var searchType: String

)