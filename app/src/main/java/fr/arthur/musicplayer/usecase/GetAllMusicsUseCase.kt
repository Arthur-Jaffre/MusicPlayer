package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.repositories.MusicRepository

class GetAllMusicsUseCase(private val repository: MusicRepository) {

    suspend fun loadCachedMusics(): List<Music> {
        return repository.loadCachedMusics()
    }

    suspend fun scanAndSaveMusics(
        onMusicFound: (Music) -> Unit,
        onComplete: () -> Unit
    ) {
        repository.scanAndSaveMusics(onMusicFound, onComplete)
    }
}

