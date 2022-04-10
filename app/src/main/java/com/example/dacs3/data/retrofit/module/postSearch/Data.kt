package com.example.dacs3.data.retrofit.module.postSearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data (

    @SerializedName("searchResults" ) @Expose var searchResults : ArrayList<SearchResults> = arrayListOf()

)
