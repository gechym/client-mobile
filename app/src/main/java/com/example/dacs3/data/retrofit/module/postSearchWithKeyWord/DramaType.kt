package com.example.example

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class DramaType(

    @SerializedName("code") @Expose var code: String,
    @SerializedName("name") @Expose var name: String

)