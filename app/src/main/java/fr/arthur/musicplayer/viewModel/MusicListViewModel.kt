package fr.arthur.musicplayer.viewModel

import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.observer.SimpleObservable
import fr.arthur.musicplayer.usecase.MusicUseCase
import fr.arthur.musicplayer.usecase.ScannerUseCase
import kotlinx.coroutines.launch

class MusicListViewModel(
    private val musicUseCase: MusicUseCase,
    private val scannerUseCase: ScannerUseCase
) : BaseListViewModel() {
    val musicsObservable = SimpleObservable<List<Music>>()
    private val musics = mutableListOf<Music>()

    fun toFavorites(music: Music) {
        scope.launch {
            musicUseCase.updateFavorites(music)
        }
    }

    fun getMusicsByArtist(artist: Artist) {
        scope.launch {
            musicsObservable.post(musicUseCase.getMusicsByArtist(artist))
        }
    }

    fun getMusicsByAlbum(album: Album) {
        scope.launch {
            musicsObservable.post(musicUseCase.getMusicsByAlbum(album))
        }
    }

    fun loadMusics() {
        musics.clear()
        musicsObservable.post(emptyList())

        scope.launch {
            val cached = musicUseCase.loadCachedMusics()

            if (cached.isEmpty()) {
                // Base vide → lancer un scan complet puis sauvegarder dans Room
                scannerUseCase.scanAndSave(
                    onMusicFound = { /* on ne met rien à jour ici */ },
                    onComplete = {
                        // Après scan, recharger la base Room
                        scope.launch {
                            musics.addAll(musicUseCase.loadCachedMusics())
                            musicsObservable.post(musics.sortedBy { it.title })
                        }
                    }
                )
            } else {
                // Base non vide → afficher directement
                musics.addAll(cached)
                musicsObservable.post(musics.sortedBy { it.title })

                // Puis lancer un scan asynchrone en fond pour mise à jour intelligente
                scannerUseCase.scanAndSave(
                    onMusicFound = { /* rien */ },
                    onComplete = {
                        scope.launch {
                            musics.clear()
                            musics.addAll(musicUseCase.loadCachedMusics())
                            musicsObservable.post(musics.sortedBy { it.title })
                        }
                    }
                )
            }
        }
    }
}
