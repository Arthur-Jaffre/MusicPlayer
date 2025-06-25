package fr.arthur.musicplayer.models

data class Playlist(
    val id: String,
    val name: String? = null,
    val numberOfMusics: Int = 0
)