package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.room.entities.MusicEntity

interface IMusicRepository {
    fun MusicEntity.toDomain(): Music {
        return Music(
            id = id,
            title = title,
            duration = duration,
            artistId = artistId,
            albumId = albumId,
            year = year,
            trackNumber = trackNumber,
            imageUri = imageUri,
            isFavorite = isFavorite
        )
    }

    suspend fun loadCachedMusics(): List<Music>
    suspend fun getMusicsFromAlbum(albumId: String): List<Music>
    suspend fun getAllFavoritesMusics(): List<Music>
    suspend fun updateFavorites(music: Music)

}