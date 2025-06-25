package fr.arthur.musicplayer.room.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithMusics(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PlaylistMusicCrossRef::class,
            parentColumn = "playlistId",
            entityColumn = "musicId"
        )
    )
    val musics: List<MusicEntity>
)
