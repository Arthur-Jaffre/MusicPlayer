package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.ArtistUseCase
import kotlinx.coroutines.launch

class ArtistListViewModel(
    private val artistUseCase: ArtistUseCase
) : BaseListViewModel() {
    val artistsObservable = SimpleObservable<List<Artist>>()

    fun loadArtists() {
        scope.launch {
            val data = artistUseCase.execute()
            artistsObservable.post(data)
        }
    }
}