package fr.arthur.musicplayer.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "playlist_music_cross_ref",
    primaryKeys = ["playlistId", "musicId"],
    foreignKeys = [
        ForeignKey(entity = PlaylistEntity::class, parentColumns = ["id"], childColumns = ["playlistId"]),
        ForeignKey(entity = MusicEntity::class, parentColumns = ["id"], childColumns = ["musicId"])
    ],
    indices = [Index("playlistId"), Index("musicId")]
)
data class PlaylistMusicCrossRef(
    val playlistId: String,
    val musicId: String
)