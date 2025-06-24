package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Playlist

interface IMusicRepository {
    fun getAllArtists(): List<Artist>
    fun getAllPlayLists(): List<Playlist>
}