package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.repositories.interfaces.IMusicRepository
import fr.arthur.musicplayer.room.DAO.MusicDAO

class MusicRepository(
    private val musicDao: MusicDAO
) : IMusicRepository {

    override suspend fun loadCachedMusics(): List<Music> {
        return musicDao.getAll().map { it.toDomain() }
    }

    override suspend fun getMusicsFromAlbum(albumId: String): List<Music> {
        return musicDao.getFromAlbum(albumId).map { it.toDomain() }
    }

    override suspend fun getAllFavoritesMusics(): List<Music> {
        return musicDao.getAllFavoritesMusics().map { it.toDomain() }
    }

    override suspend fun addMusicToFavorites(music: Music) {
        musicDao.addMusicToFavorites(music.id)
    }

}