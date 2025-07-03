package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Playlist

interface IPlaylistRepository {
    suspend fun getAllPlayLists(): List<Playlist>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun insertMusic(playlistId: String, musicId: String)
    suspend fun updatePlaylist(playlist: Playlist)
}