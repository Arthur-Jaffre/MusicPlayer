package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.repositories.interfaces.IMusicRepository

class MusicUseCase(private val repository: IMusicRepository) {

    suspend fun loadCachedMusics(): List<Music> {
        return repository.loadCachedMusics()
    }

    suspend fun getAllFavoritesMusics(): List<Music> {
        return repository.getAllFavoritesMusics()
    }

    suspend fun updateFavorites(music: Music) {
        repository.updateFavorites(music)
    }

    suspend fun getRecentlyAdded(): List<Music> {
        return repository.getRecentlyAdded()
    }

    suspend fun getMusicsByArtist(artist: Artist): List<Music> {
        return repository.getMusicsByArtist(artist.id)
    }

}
