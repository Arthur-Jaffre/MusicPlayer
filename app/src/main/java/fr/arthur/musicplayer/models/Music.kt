package fr.arthur.musicplayer.models

data class Music(
    val id: String,
    val title: String,
    val artist: Artist,
    val duration: Int // en secondes
)