package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.repositories.interfaces.IArtistRepository

class GetAllArtistsUseCase(private val repository: IArtistRepository) {
    suspend fun execute(): List<Artist> = repository.getAllArtists()
}