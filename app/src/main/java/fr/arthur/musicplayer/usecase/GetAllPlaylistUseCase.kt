package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.repositories.IMusicRepository
import fr.arthur.musicplayer.repositories.MusicRepository

class GetAllPlaylistUseCase(private val repository: MusicRepository) {
    suspend fun execute(): List<Playlist> = repository.getAllPlayLists()
}