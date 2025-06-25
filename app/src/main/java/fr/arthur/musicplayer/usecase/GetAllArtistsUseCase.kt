package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.repositories.IMusicRepository
import fr.arthur.musicplayer.repositories.MusicRepository

class GetAllArtistsUseCase(private val repository: MusicRepository) {
    suspend fun execute(): List<Artist> = repository.getAllArtists()
}