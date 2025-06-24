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
) {
    val musicsObservable = SimpleObservable<List<Music>>()
    private val musics = mutableListOf<Music>()

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun loadMusics() {
        musics.clear()
        musicsObservable.post(emptyList())

        scope.launch {
            getAllMusicsUseCase.executeAsync(
                onMusicFound = { music ->
                    musics.add(music)
                    musicsObservable.post(musics.toList().sortedBy { it.title })
                },
                onComplete = {
                    // Optionnel : notifier fin du chargement
                }
            )
        }
    }

    fun onCleared() {
        job.cancel()
    }
}