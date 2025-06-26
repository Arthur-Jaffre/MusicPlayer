package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.repositories.interfaces.IAlbumRepository
import fr.arthur.musicplayer.room.DAO.AlbumDAO

class AlbumRepository(
    private val albumDao: AlbumDAO
) : IAlbumRepository {

    override suspend fun getAllAlbums(): List<Album> {
        return albumDao.getAll().map {
            Album(id = it.id, name = it.name, artistId = it.artistId)
        }
    }

    override suspend fun getAlbumsByArtist(artistId: String): List<Album> {
        return albumDao.getFromArtist(artistId).map {
            Album(id = it.id, name = it.name, artistId = it.artistId)
        }
    }
}