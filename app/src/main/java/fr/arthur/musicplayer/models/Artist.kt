package fr.arthur.musicplayer.models

import java.io.Serializable

data class Artist(
    val id: String, // nom unique de l'artiste
    val imageUri: String? = null
) : Serializable