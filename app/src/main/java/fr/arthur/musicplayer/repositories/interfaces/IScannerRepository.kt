package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Music

interface IScannerRepository {
    suspend fun scanAndSaveEverything(onMusicFound: (Music) -> Unit, onComplete: () -> Unit)
}