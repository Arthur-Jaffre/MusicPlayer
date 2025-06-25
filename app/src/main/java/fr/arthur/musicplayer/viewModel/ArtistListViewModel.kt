package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.GetAllArtistsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArtistListViewModel(
    private val getAllArtistsUseCase: GetAllArtistsUseCase
) : BaseListViewModel() {
    val artistsObservable = SimpleObservable<List<Artist>>()

    fun loadArtists() {
        scope.launch {
            val data = getAllArtistsUseCase.execute()
            artistsObservable.post(data)
        }
    }
}