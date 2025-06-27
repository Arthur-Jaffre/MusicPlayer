package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.repositories.interfaces.IMusicRepository
import fr.arthur.musicplayer.room.DAO.MusicDAO
import fr.arthur.musicplayer.room.entities.MusicArtistCrossRef
import fr.arthur.musicplayer.room.entities.MusicEntity

class MusicRepository(
    private val musicDao: MusicDAO
) : IMusicRepository {

    override suspend fun loadCachedMusics(): List<Music> {
        return musicDao.getAll().map { it.toDomain() }
    }

    override suspend fun getMusicsFromAlbum(albumId: String): List<Music> {
        return musicDao.getFromAlbum(albumId).map { it.toDomain() }.sortedWith(
            compareBy(
                { it.trackNumber == null }, // met les non-null
                { it.trackNumber ?: Int.MAX_VALUE },  // tri par trackNumber si non null
                { it.title }                          // tri par titre ensuite
            ))
    }

    override suspend fun getAllFavoritesMusics(): List<Music> {
        return musicDao.getAllFavoritesMusics().map { it.toDomain() }
    }

    override suspend fun updateFavorites(music: Music) {
        musicDao.updateFavorites(music.id, if (music.isFavorite) 1 else 0)
    }

    override suspend fun getRecentlyAdded(): List<Music> {
        return musicDao.getRecentlyAdded().map { it.toDomain() }
    }

    override suspend fun getMusicsByArtist(artistId: String): List<Music> {
        return musicDao.getFromArtist(artistId).map { it.toDomain() }
    }

    override suspend fun updateMusic(music: Music) {
        musicDao.insertAll(
            listOf(
                MusicEntity(
                    id = music.id,
                    title = music.title,
                    albumId = music.albumId,
                    duration = music.duration,
                    year = music.year,
                    trackNumber = music.trackNumber,
                    imageUri = music.imageUri,
                    isFavorite = music.isFavorite,
                    addedAt = music.addedAt
                )
            )
        )

        musicDao.deleteArtistCrossRefs(music.id)
        musicDao.insertArtistCrossRefs(music.artistIds.map { MusicArtistCrossRef(music.id, it) })
    }

}