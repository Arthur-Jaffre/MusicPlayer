package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.repositories.IMusicRepository

class GetAllArtistsUseCase(private val repository: IMusicRepository) {
    fun execute(): List<Artist> = repository.getAllArtists()
}