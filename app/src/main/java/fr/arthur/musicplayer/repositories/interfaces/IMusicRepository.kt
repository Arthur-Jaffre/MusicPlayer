package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.room.entities.MusicEntity
import fr.arthur.musicplayer.room.entities.MusicWithArtists

interface IMusicRepository {
    fun MusicWithArtists.toDomain(): Music {
        return Music(
            id = music.id,
            title = music.title,
            duration = music.duration,
            albumId = music.albumId,
            year = music.year,
            trackNumber = music.trackNumber,
            imageUri = music.imageUri,
            isFavorite = music.isFavorite,
            addedAt = music.addedAt,
            artistIds = artists.map { it.id }
        )
    }

    fun Music.toEntity(): MusicEntity = MusicEntity(
        id = id,
        title = title,
        albumId = albumId,
        duration = duration,
        year = year,
        trackNumber = trackNumber,
        imageUri = imageUri,
        isFavorite = isFavorite,
        addedAt = addedAt
    )

    suspend fun loadCachedMusics(): List<Music>
    suspend fun getMusicsFromAlbum(albumId: String): List<Music>
    suspend fun getAllFavoritesMusics(): List<Music>
    suspend fun updateFavorites(music: Music)
    suspend fun getRecentlyAdded(): List<Music>
    suspend fun getMusicsByArtist(artistId: String): List<Music>
    suspend fun updateMusic(music: Music, albumName: String?)
}