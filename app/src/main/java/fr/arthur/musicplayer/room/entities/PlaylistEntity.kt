package fr.arthur.musicplayer.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val numberOfMusics: Int
)
