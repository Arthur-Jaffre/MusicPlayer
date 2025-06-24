package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.repositories.IMusicRepository

class GetAllPlaylistUseCase(private val repository: IMusicRepository) {
    fun execute(): List<Playlist> = repository.getAllPlayLists()
}