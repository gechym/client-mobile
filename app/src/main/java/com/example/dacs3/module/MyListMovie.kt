package com.example.dacs3.module

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class MyListMovie(
    val id : Int? = null,
    val category: Int? = null,
    val image: String? = null

)
