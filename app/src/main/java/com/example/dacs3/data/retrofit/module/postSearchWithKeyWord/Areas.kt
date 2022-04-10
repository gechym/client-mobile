package com.example.example

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Areas (

  @SerializedName("id"   ) @Expose var id   : Int,
  @SerializedName("name" ) @Expose var name : String

)