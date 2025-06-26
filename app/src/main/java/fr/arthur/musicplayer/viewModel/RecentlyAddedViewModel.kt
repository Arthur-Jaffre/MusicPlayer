package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.MusicUseCase
import kotlinx.coroutines.launch

class RecentlyAddedViewModel(
    private val musicUseCase: MusicUseCase
) : BaseListViewModel() {
    val recentlyAddedObservable = SimpleObservable<List<Music>>()

    fun toFavorites(music: Music) {
        scope.launch {
            musicUseCase.updateFavorites(music)
        }
    }

    fun loadRecentlyAdded() {
        scope.launch {
            recentlyAddedObservable.post(musicUseCase.getRecentlyAdded())
        }
    }

    fun getRecentlyAddedCount(): Int {
        return recentlyAddedObservable.value?.size ?: 0
    }
}
