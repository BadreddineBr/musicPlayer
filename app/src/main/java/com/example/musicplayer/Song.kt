package com.example.musicplayer.data

import java.io.Serializable

data class Song(
    val title: String,
    val artist: String,
    val coverImage: Int,
    val audioResId: Int,
) : Serializable
