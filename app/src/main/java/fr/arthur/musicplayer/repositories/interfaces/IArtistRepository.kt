package fr.arthur.musicplayer.repositories.interfaces

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.room.entities.ArtistEntity

interface IArtistRepository {
    suspend fun getAllArtists(): List<Artist>
    suspend fun getArtistsById(ids: List<String>): List<ArtistEntity>
    suspend fun insertArtist(id: String, imageUri: String?): ArtistEntity
    suspend fun deleteOrphanArtists()
    suspend fun getArtistById(id: String): Artist?
    suspend fun deleteArtistById(id: String)
    suspend fun getAllArtistIds(): List<String>
    suspend fun updateArtist(artist: Artist)
}