package com.example.dacs3.module

data class Film (
    val id : Int,
    val name : String,
    val image : Int,
    val imageUrl : String,
    val banner : String,
    val description : String,
    val category: Int,
    val epvisodeTotal : Int,
    val yearRelease : Int
)