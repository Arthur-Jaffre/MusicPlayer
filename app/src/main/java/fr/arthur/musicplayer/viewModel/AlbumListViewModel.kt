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
    val albumObservable = SimpleObservable<Album>()

    fun getAlbumById(id: String) {
        scope.launch {
            albumObservable.post(albumUseCase.getAlbumById(id))
        }
    }

    fun updateAlbum(album: Album, oldArtist: String) {
        scope.launch {
            albumUseCase.updateAlbum(album, oldArtist)
            albumObservable.post(albumUseCase.getAlbumById(album.id))
        }
    }

    fun getAlbumsByArtist(artist: Artist) {
        scope.launch {
            albumsObservable.post(albumUseCase.getAlbumsByArtist(artist.id))
        }
    }
}