package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.repositories.interfaces.IAlbumRepository

class AlbumUseCase(private val repository: IAlbumRepository) {
    suspend fun getAlbumsByArtist(artistId: String): List<Album> =
        repository.getAlbumsByArtist(artistId)

    suspend fun getAlbumById(albumId: String): Album =
        repository.getAlbumById(albumId)
}