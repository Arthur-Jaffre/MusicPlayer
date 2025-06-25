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
            val playlist = playlistUseCase.execute()
            playlistObservable.post(playlist)
        }
    }
}