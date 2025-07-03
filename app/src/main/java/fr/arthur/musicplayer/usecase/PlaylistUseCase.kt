package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.repositories.interfaces.IPlaylistRepository

class PlaylistUseCase(private val repository: IPlaylistRepository) {
    suspend fun getAllPlayLists(): List<Playlist> = repository.getAllPlayLists()
    suspend fun addPlaylist(playlist: Playlist) = repository.addPlaylist(playlist)
    suspend fun updatePlaylist(playlist: Playlist) = repository.updatePlaylist(playlist)
    suspend fun insertMusic(playlist: Playlist, musicId: String) =
        repository.insertMusic(playlist.id, musicId)

    suspend fun deletePlaylist(playlist: Playlist) {
        // Supprimer toutes les références de la playlist dans la table de jointure
        repository.deletePlaylistMusicCrossRef(playlist.id)
        // Supprimer la playlist
        repository.deletePlaylist(playlist)
    }

}