package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.helpers.SmartSearch
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.ArtistUseCase
import fr.arthur.musicplayer.usecase.MusicUseCase
import kotlinx.coroutines.launch

class SearchViewModel(
    private val artistUseCase: ArtistUseCase,
    private val musicUseCase: MusicUseCase
) : BaseListViewModel() {
    val musicsObservable = SimpleObservable<List<Music>>()
    val artistsObservable = SimpleObservable<List<Artist>>()

    fun searchByMusic(query: String) {
        scope.launch {
            val result =
                SmartSearch.search(musicUseCase.loadCachedMusics(), query) { it.title.toString() }
            musicsObservable.post(result)
        }
    }

    fun searchByArtist(query: String) {
        scope.launch {
            val result =
                SmartSearch.search(artistUseCase.getAllArtists(), query) { it.id }
            artistsObservable.post(result)
        }
    }
}