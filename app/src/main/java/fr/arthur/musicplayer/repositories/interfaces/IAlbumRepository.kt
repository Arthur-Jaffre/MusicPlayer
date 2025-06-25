package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Album

interface IAlbumRepository {
    suspend fun getAllAlbums(): List<Album>
    suspend fun getAlbumsFromArtist(artistId: String): List<Album>
}