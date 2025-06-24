package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.GetAllPlaylistUseCase

class PlayListListViewModel(
    private val getAllPlaylistUseCase: GetAllPlaylistUseCase
) {
    val musicsObservable = SimpleObservable<List<Playlist>>()

    fun loadPlaylists() {
        val playlist = getAllPlaylistUseCase.execute()
        musicsObservable.post(playlist)
    }
}