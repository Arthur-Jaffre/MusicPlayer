package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Playlist

interface IPlaylistRepository {
    suspend fun getAllPlayLists(): List<Playlist>
}