package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.GetAllPlaylistUseCase
import kotlinx.coroutines.launch

class PlayListListViewModel(
    private val getAllPlaylistUseCase: GetAllPlaylistUseCase
): BaseListViewModel() {
    val playlistObservable = SimpleObservable<List<Playlist>>()

    fun loadPlaylists() {
        scope.launch {
            val playlist = getAllPlaylistUseCase.execute()
            playlistObservable.post(playlist)
        }
    }
}