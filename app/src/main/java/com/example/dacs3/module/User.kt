package com.example.dacs3.module

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class User(
    val email: String? = null,
    val avata: String? = null,
    val isMenbership: Boolean? = null,
    val myListMovie: MyListMovie = MyListMovie()
)