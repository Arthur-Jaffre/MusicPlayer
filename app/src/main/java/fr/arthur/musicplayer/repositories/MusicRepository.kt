package fr.arthur.musicplayer.repositories

import androidx.room.Transaction
import fr.arthur.musicplayer.helpers.AudioMetadataUpdater
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.repositories.interfaces.IMusicRepository
import fr.arthur.musicplayer.room.DAO.MusicDAO
import fr.arthur.musicplayer.room.entities.MusicArtistCrossRef

class MusicRepository(
    private val musicDao: MusicDAO,
    private val audioMetadataUpdater: AudioMetadataUpdater
) : IMusicRepository {

    override suspend fun loadCachedMusics(): List<Music> {
        return musicDao.getAll().map { it.toDomain() }.sortedBy { it.artistIds.first() }
    }

    override suspend fun getMusicsFromAlbum(albumId: String): List<Music> {
        return musicDao.getFromAlbum(albumId).map { it.toDomain() }.sortedWith(
            compareBy(
                { it.trackNumber == null }, // met les non-null
                { it.trackNumber ?: Int.MAX_VALUE },  // tri par trackNumber si non null
                { it.artistIds.first() } // tri par artiste ensuite
            ))
    }

    override suspend fun getAllFavoritesMusics(): List<Music> {
        return musicDao.getAllFavoritesMusics().map { it.toDomain() }
            .sortedBy { it.artistIds.first() }
    }

    override suspend fun updateFavorites(music: Music) {
        musicDao.updateFavorites(music.id, if (music.isFavorite) 1 else 0)
    }

    override suspend fun getRecentlyAdded(): List<Music> {
        return musicDao.getRecentlyAdded().map { it.toDomain() }.sortedBy { it.artistIds.first() }
    }

    override suspend fun getMusicsByArtist(artistId: String): List<Music> {
        return musicDao.getFromArtist(artistId).map { it.toDomain() }
            .sortedBy { it.artistIds.first() }
    }

    @Transaction
    override suspend fun updateMusic(music: Music, albumName: String?) {
        audioMetadataUpdater.updateAudioMetadata(music, albumName)

        val refs = music.artistIds.map { MusicArtistCrossRef(music.id, it) }
        musicDao.updateMusicWithRelations(music.toEntity(), refs)
    }

}