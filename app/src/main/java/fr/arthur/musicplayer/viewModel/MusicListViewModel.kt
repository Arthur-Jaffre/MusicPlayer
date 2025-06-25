package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.GetAllMusicsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MusicListViewModel(
    private val getAllMusicsUseCase: GetAllMusicsUseCase
) : BaseListViewModel() {
    val musicsObservable = SimpleObservable<List<Music>>()
    private val musics = mutableListOf<Music>()

    fun loadMusics() {
        musics.clear()
        musicsObservable.post(emptyList())

        scope.launch {
            val cached = getAllMusicsUseCase.loadCachedMusics()

            if (cached.isEmpty()) {
                // Base vide → lancer un scan complet puis sauvegarder dans Room
                getAllMusicsUseCase.scanAndSaveMusics(
                    onMusicFound = { /* on ne met rien à jour ici */ },
                    onComplete = {
                        // Après scan, recharger la base Room
                        scope.launch {
                            val updated = getAllMusicsUseCase.loadCachedMusics()
                            musics.addAll(updated)
                            musicsObservable.post(musics.sortedBy { it.title })
                        }
                    }
                )
            } else {
                // Base non vide → afficher directement
                musics.addAll(cached)
                musicsObservable.post(musics.sortedBy { it.title })

                // Puis lancer un scan asynchrone en fond pour mise à jour intelligente
                getAllMusicsUseCase.scanAndSaveMusics(
                    onMusicFound = { /* rien */ },
                    onComplete = {
                        scope.launch {
                            musics.clear()
                            val updated = getAllMusicsUseCase.loadCachedMusics()
                            musics.addAll(updated)
                            musicsObservable.post(musics.sortedBy { it.title })
                        }
                    }
                )
            }
        }
    }


    fun onCleared() {
        job.cancel()
    }
}
