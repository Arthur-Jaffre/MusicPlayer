package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.MusicUseCase
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val musicUseCase: MusicUseCase
) : BaseListViewModel() {

    val favoritesObservable = SimpleObservable<List<Music>>()

    fun loadFavorites() {
        scope.launch {
            val favorites = musicUseCase.getAllFavoritesMusics()
            favoritesObservable.post(favorites)
        }
    }

    fun toFavorites(music: Music) {
        scope.launch {
            musicUseCase.updateFavorites(music)
        }
        // mettre à jour la liste des favoris
        loadFavorites()
    }

    fun getFavoritesCount(): Int {
        return favoritesObservable.value?.size ?: 0
    }
}