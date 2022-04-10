package com.example.dacs3.data.retrofit.module.getSearchConfig

import com.google.gson.annotations.SerializedName

data class Items(

    @SerializedName("name") var name: String? = null,
    @SerializedName("params") var params: String? = null,
    @SerializedName("screeningType") var screeningType: String? = null

)
