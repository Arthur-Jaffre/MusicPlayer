package fr.arthur.musicplayer.models

data class Music(
    val id: String,
    val title: String? = null,
    val artistId: String,
    val albumId: String,
    val duration: Int? = null,
    val year: Int? = null,
    val trackNumber: Int? = null,
    val imageUri: String? = null,
    val isFavorite: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)