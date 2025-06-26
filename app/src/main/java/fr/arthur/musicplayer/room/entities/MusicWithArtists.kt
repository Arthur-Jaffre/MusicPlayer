package fr.arthur.musicplayer.room.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MusicWithArtists(
    @Embedded val music: MusicEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MusicArtistCrossRef::class,
            parentColumn = "musicId",
            entityColumn = "artistId"
        )
    )
    val artists: List<ArtistEntity>
)
