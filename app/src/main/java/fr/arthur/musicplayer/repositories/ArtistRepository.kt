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
            Artist(id = it.id, imageUri = it.imageUri)
        }.sortedBy { it.id }
    }

    override suspend fun getArtistsById(ids: List<String>): List<ArtistEntity> {
        return artistDao.getByIds(ids)
    }

    override suspend fun insertArtist(id: String, imageUri: String?): ArtistEntity {
        val artist = ArtistEntity(id = id, imageUri = imageUri)
        artistDao.insert(artist)
        return artist
    }

    override suspend fun deleteOrphanArtists() {
        artistDao.deleteOrphanArtists()
    }

    override suspend fun getArtistById(id: String): Artist? {
        return artistDao.getById(id)?.let {
            Artist(id = it.id, imageUri = it.imageUri)
        }
    }

    override suspend fun deleteArtistById(id: String) {
        artistDao.deleteById(id)
    }

    override suspend fun getAllArtistIds(): List<String> {
        return artistDao.getAllArtistIds()
    }

    override suspend fun updateArtist(artist: Artist) {
        artistDao.update(ArtistEntity(id = artist.id, imageUri = artist.imageUri))
    }
}
