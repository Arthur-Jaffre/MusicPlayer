package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.repositories.interfaces.IPlaylistRepository

class PlaylistUseCase(private val repository: IPlaylistRepository) {
    suspend fun execute(): List<Playlist> = repository.getAllPlayLists()
}