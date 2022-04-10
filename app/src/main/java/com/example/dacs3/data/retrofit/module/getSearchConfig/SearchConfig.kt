package com.example.dacs3.data.retrofit.module.getSearchConfig

import com.google.gson.annotations.SerializedName

data class SearchConfig (

    @SerializedName("code" ) var code : String?         = null,
    @SerializedName("data" ) var data : ArrayList<Data> = arrayListOf(),
    @SerializedName("msg"  ) var msg  : String?         = null

)
