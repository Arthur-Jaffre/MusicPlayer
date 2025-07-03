package fr.arthur.musicplayer.models

import java.io.Serializable

data class Playlist(
    val id: String,
    val name: String? = null,
    val numberOfMusics: Int = 0
) : Serializable