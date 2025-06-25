package fr.arthur.musicplayer.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "album",
    foreignKeys = [
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artistId"]
        )
    ],
    indices = [Index("artistId")]
)
data class AlbumEntity(
    @PrimaryKey val id: String,
    val name: String? = null,
    val artistId: String,
    val imageUri: String? = null
)