package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.repositories.interfaces.IAlbumRepository
import fr.arthur.musicplayer.room.DAO.AlbumDAO
import fr.arthur.musicplayer.room.entities.AlbumEntity

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

    override suspend fun getAlbumById(albumId: String): Album {
        return albumDao.getById(albumId)
            .let { album -> Album(id = album.id, name = album.name, artistId = album.artistId) }
    }

    override suspend fun getAlbumsByName(name: String): List<Album> {
        return albumDao.getByName(name).map {
            Album(id = it.id, name = it.name, artistId = it.artistId)
        }
    }

    override suspend fun addAlbum(album: Album) {
        albumDao.insert(album.let { album ->
            AlbumEntity(
                id = album.id,
                name = album.name,
                artistId = album.artistId
            )
        })
    }

    override suspend fun deleteOrphanAlbums() {
        albumDao.deleteOrphanAlbums()
    }

    override suspend fun updateAlbum(album: Album) {
        albumDao.update(album.let { album ->
            AlbumEntity(
                id = album.id,
                name = album.name,
                artistId = album.artistId
            )
        })
    }
}