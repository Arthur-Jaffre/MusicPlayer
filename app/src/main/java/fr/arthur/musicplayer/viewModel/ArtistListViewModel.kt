package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.GetAllArtistsUseCase

class ArtistListViewModel(
    private val getAllArtistsUseCase: GetAllArtistsUseCase
) {
    val musicsObservable = SimpleObservable<List<Artist>>()

    fun loadArtists() {
        val artists = getAllArtistsUseCase.execute()
        musicsObservable.post(artists)
    }
}