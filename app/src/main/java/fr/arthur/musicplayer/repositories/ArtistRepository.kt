package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.repositories.interfaces.IArtistRepository
import fr.arthur.musicplayer.room.DAO.ArtistDAO
import fr.arthur.musicplayer.room.entities.ArtistEntity

class ArtistRepository(
    private val artistDao: ArtistDAO
) : IArtistRepository {

    override suspend fun getAllArtists(): List<Artist> {
        return artistDao.getAll().map {
            Artist(id = it.id)
        }.sortedBy { it.id }
    }

    override suspend fun getArtistsById(ids: List<String>): List<ArtistEntity> {
        return artistDao.getByIds(ids)
    }

    override suspend fun insertArtist(id: String): ArtistEntity {
        val artist = ArtistEntity(id = id)
        artistDao.insert(artist)
        return artist
    }
}
