package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Music

interface IMusicRepository {
    suspend fun loadCachedMusics(): List<Music>
    suspend fun getMusicsFromAlbum(albumId: String): List<Music>
}