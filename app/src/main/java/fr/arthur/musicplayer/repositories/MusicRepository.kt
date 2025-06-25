package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.helpers.AppConstants.UNKNOWN_ITEM
import fr.arthur.musicplayer.helpers.MusicScanner
import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.repositories.interfaces.IMusicRepository
import fr.arthur.musicplayer.room.DAO.AlbumDAO
import fr.arthur.musicplayer.room.DAO.ArtistDAO
import fr.arthur.musicplayer.room.DAO.MusicDAO
import fr.arthur.musicplayer.room.DAO.PlaylistDAO
import fr.arthur.musicplayer.room.entities.AlbumEntity
import fr.arthur.musicplayer.room.entities.ArtistEntity
import fr.arthur.musicplayer.room.entities.MusicEntity

class MusicRepository(
    private val musicDao: MusicDAO
) : IMusicRepository {

    override suspend fun loadCachedMusics(): List<Music> {
        return musicDao.getAll().map {
            Music(
                id = it.id,
                title = it.title,
                duration = it.duration,
                artistId = it.artistId,
                albumId = it.albumId,
                year = it.year,
                trackNumber = it.trackNumber,
                imageUri = it.imageUri,
                isFavorite = it.isFavorite
            )
        }
    }

    override suspend fun getMusicsFromAlbum(albumId: String): List<Music> {
        return musicDao.getFromAlbum(albumId).map {
            Music(
                id = it.id,
                title = it.title,
                duration = it.duration,
                artistId = it.artistId,
                albumId = it.albumId,
                year = it.year,
                trackNumber = it.trackNumber,
                imageUri = it.imageUri,
                isFavorite = it.isFavorite
            )
        }
    }
}