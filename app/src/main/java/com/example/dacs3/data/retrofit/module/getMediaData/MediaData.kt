package com.example.dacs3.data.retrofit.module.getMediaData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MediaData (

    @SerializedName("status" ) @Expose var status : Int?  = null,
    @SerializedName("data"   ) @Expose var data   : Data? = Data()

)