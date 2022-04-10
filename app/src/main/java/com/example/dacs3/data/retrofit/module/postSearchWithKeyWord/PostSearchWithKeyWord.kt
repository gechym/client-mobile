package com.example.example

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class PostSearchWithKeyWord(
    @SerializedName("data") @Expose var data: Data
)