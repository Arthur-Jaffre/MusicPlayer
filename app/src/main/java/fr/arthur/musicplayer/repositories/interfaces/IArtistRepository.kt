package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Artist

interface IArtistRepository {
    suspend fun getAllArtists(): List<Artist>
}