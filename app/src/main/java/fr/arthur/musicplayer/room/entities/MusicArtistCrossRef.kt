package fr.arthur.musicplayer.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["musicId", "artistId"],
    foreignKeys = [
        ForeignKey(entity = MusicEntity::class, parentColumns = ["id"], childColumns = ["musicId"]),
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artistId"]
        )
    ],
    indices = [Index("artistId"), Index("musicId")]
)
data class MusicArtistCrossRef(
    val musicId: String,
    val artistId: String
)
