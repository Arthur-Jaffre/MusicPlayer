package fr.arthur.musicplayer.models

import java.io.Serializable

data class Music(
    val id: String,
    val title: String? = null,
    val artistIds: List<String>,
    val albumId: String,
    val duration: Int? = null,
    val year: Int? = null,
    val trackNumber: Int? = null,
    val imageUri: String? = null,
    val isFavorite: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
) : Serializable {
    fun artistIdsAsString(separator: String = ", "): String {
        return artistIds.joinToString(separator)
    }
}