package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.repositories.interfaces.IArtistRepository

class ArtistUseCase(private val repository: IArtistRepository) {
    suspend fun execute(): List<Artist> = repository.getAllArtists()
    suspend fun getArtistById(id: String) = repository.getArtistById(id)
}