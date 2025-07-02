package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.repositories.interfaces.IPlaylistRepository

class PlaylistUseCase(private val repository: IPlaylistRepository) {
    suspend fun getAllPlayLists(): List<Playlist> = repository.getAllPlayLists()
    suspend fun addPlaylist(playlist: Playlist) = repository.addPlaylist(playlist)

    suspend fun insertMusic(playlistId: String, musicId: String) =
        repository.insertMusic(playlistId, musicId)
}