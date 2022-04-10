package com.example.dacs3.data.retrofit.module.getSearchConfig

import com.google.gson.annotations.SerializedName

data class ScreeningItems(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("items") var items: ArrayList<Items> = arrayListOf(),
    @SerializedName("name") var name: String? = null

)
