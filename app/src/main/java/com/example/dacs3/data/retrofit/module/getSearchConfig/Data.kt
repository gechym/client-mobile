package com.example.dacs3.data.retrofit.module.getSearchConfig

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("params") var params: String? = null,
    @SerializedName("screeningItems") var screeningItems: ArrayList<ScreeningItems> = arrayListOf()
)