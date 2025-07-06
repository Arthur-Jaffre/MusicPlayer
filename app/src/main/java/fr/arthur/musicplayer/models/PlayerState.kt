package fr.arthur.musicplayer.models

data class PlayerState(
    val music: Music?,
    val isPlaying: Boolean,
    val position: Long,
    val duration: Long
)
