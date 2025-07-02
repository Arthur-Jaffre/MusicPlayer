package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Album

interface IAlbumRepository {
    suspend fun getAllAlbums(): List<Album>
    suspend fun getAlbumsByArtist(artistId: String): List<Album>
    suspend fun getAlbumById(albumId: String): Album
    suspend fun getAlbumsByName(name: String): List<Album>
    suspend fun addAlbum(album: Album)
    suspend fun deleteOrphanAlbums()
    suspend fun updateAlbum(album: Album)
}