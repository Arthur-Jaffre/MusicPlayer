package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.repositories.interfaces.IArtistRepository
import fr.arthur.musicplayer.room.DAO.ArtistDAO

class ArtistRepository(
    private val artistDao: ArtistDAO
) : IArtistRepository {

    override suspend fun getAllArtists(): List<Artist> {
        return artistDao.getAll().map {
            Artist(id = it.id)
        }
    }
}
