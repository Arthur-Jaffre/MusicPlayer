package fr.arthur.musicplayer.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist")
data class ArtistEntity(
    @PrimaryKey val id: String,
    val imageUri: String? = null
)