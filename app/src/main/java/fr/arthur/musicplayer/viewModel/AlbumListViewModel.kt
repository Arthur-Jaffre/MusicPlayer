package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.AlbumUseCase
import kotlinx.coroutines.launch

class AlbumListViewModel(
    private val albumUseCase: AlbumUseCase
) : BaseListViewModel() {
    val albumsObservable = SimpleObservable<List<Album>>()

    fun getAlbumsByArtist(artist: Artist) {
        scope.launch {
            albumsObservable.post(albumUseCase.getAlbumsByArtist(artist.id))
        }
    }
}