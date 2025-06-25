package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.repositories.interfaces.IScannerRepository

class ScannerUseCase(private val repository: IScannerRepository) {

    suspend fun scanAndSave(
        onMusicFound: (Music) -> Unit,
        onComplete: () -> Unit
    ) {
        repository.scanAndSaveEverything(onMusicFound, onComplete)
    }
}
