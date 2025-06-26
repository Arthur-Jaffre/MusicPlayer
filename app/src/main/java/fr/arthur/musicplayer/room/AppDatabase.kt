package fr.arthur.musicplayer.room

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.arthur.musicplayer.room.DAO.AlbumDAO
import fr.arthur.musicplayer.room.DAO.ArtistDAO
import fr.arthur.musicplayer.room.DAO.MusicArtistDAO
import fr.arthur.musicplayer.room.DAO.MusicDAO
import fr.arthur.musicplayer.room.DAO.PlaylistDAO
import fr.arthur.musicplayer.room.entities.AlbumEntity
import fr.arthur.musicplayer.room.entities.ArtistEntity
import fr.arthur.musicplayer.room.entities.MusicArtistCrossRef
import fr.arthur.musicplayer.room.entities.MusicEntity
import fr.arthur.musicplayer.room.entities.PlaylistEntity
import fr.arthur.musicplayer.room.entities.PlaylistMusicCrossRef

@Database(
    version = 6,
    entities = [
        MusicEntity::class,
        ArtistEntity::class,
        AlbumEntity::class,
        PlaylistEntity::class,
        PlaylistMusicCrossRef::class,
        MusicArtistCrossRef::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDAO
    abstract fun artistDao(): ArtistDAO
    abstract fun albumDao(): AlbumDAO
    abstract fun playlistDao(): PlaylistDAO
    abstract fun MusicArtistDAO(): MusicArtistDAO
}
