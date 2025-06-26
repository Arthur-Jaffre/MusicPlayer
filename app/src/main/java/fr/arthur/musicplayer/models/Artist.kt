package fr.arthur.musicplayer.models

import java.io.Serializable

data class Artist(
    val id: String,
    val imageUri: String? = null
) : Serializable