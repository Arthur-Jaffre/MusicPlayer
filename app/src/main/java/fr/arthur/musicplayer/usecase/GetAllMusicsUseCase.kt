package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.repositories.MusicRepository

class GetAllMusicsUseCase(private val repository: MusicRepository) {

    fun executeAsync(
        onMusicFound: (Music) -> Unit,
        onComplete: () -> Unit
    ) {
        repository.scanAudioFiles(onMusicFound, onComplete)
    }
}