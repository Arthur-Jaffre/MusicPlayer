package fr.arthur.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.arthur.musicplayer.helpers.Event
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.ArtistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistListViewModel(
    private val artistUseCase: ArtistUseCase
) : BaseListViewModel() {
    val artistsObservable = SimpleObservable<List<Artist>>()
    private val _artistEvent = MutableLiveData<Event<Artist>>()
    val artistEvent: LiveData<Event<Artist>> = _artistEvent

    fun loadArtists() {
        scope.launch {
            val data = artistUseCase.loadArtists()
            artistsObservable.post(data)
        }
    }

    fun updateArtist(updatedArtist: Artist, lastName: String) {
        scope.launch(Dispatchers.IO) {
            artistUseCase.updateArtist(updatedArtist, lastName)
            _artistEvent.postValue(Event(artistUseCase.getArtistById(updatedArtist.id)!!))
        }
    }

    fun getArtistById(id: String) {
        scope.launch {
            _artistEvent.postValue(Event(artistUseCase.getArtistById(id)!!))
        }
    }
}