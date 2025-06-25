package fr.arthur.musicplayer.models

data class Album(
    val id: String,
    val name: String? = null,
    val artistId: String,
    val imageUri: String? = null
)