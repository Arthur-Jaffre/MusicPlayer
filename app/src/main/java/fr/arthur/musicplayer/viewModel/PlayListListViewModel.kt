package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.PlaylistUseCase
import kotlinx.coroutines.launch

class PlayListListViewModel(
    private val playlistUseCase: PlaylistUseCase
) : BaseListViewModel() {
    val playlistObservable = SimpleObservable<List<Playlist>>()

    fun loadPlaylists() {
        scope.launch {
            val playlist = playlistUseCase.getAllPlayLists()
            playlistObservable.post(playlist)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        scope.launch {
            playlistUseCase.deletePlaylist(playlist)
            loadPlaylists()
        }
    }

    fun addPlaylist(playlist: Playlist) {
        scope.launch {
            playlistUseCase.addPlaylist(playlist)
            loadPlaylists()
        }
    }

    fun insertMusic(playlist: Playlist, musicId: String) {
        scope.launch {
            playlistUseCase.insertMusic(playlist, musicId)
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        scope.launch {
            playlistUseCase.updatePlaylist(playlist)
            loadPlaylists()
        }

    }
}