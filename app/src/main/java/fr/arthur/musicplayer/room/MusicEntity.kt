package fr.arthur.musicplayer.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class MusicEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artistName: String,
    val duration: Int
) {
    override fun equals(other: Any?) = other is MusicEntity &&
            id == other.id &&
            title == other.title &&
            artistName == other.artistName &&
            duration == other.duration

    override fun hashCode() = id.hashCode()
}
