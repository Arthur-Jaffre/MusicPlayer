package fr.arthur.musicplayer.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "music",
    foreignKeys = [
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artistId"]
        ),
        ForeignKey(entity = AlbumEntity::class, parentColumns = ["id"], childColumns = ["albumId"])
    ],
    indices = [Index("artistId"), Index("albumId")]
)
data class MusicEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val artistId: String,
    val albumId: String,
    val duration: Int?,
    val year: Int?,
    val trackNumber: Int?,
    val imageUri: String?,
    val isFavorite: Boolean,
    val addedAt: Long
) {
    override fun equals(other: Any?) = other is MusicEntity &&
            id == other.id &&
            title == other.title &&
            artistId == other.artistId &&
            albumId == other.albumId &&
            duration == other.duration &&
            year == other.year &&
            trackNumber == other.trackNumber &&
            imageUri == other.imageUri &&
            isFavorite == other.isFavorite &&
            addedAt == other.addedAt

    override fun hashCode() = id.hashCode()
}