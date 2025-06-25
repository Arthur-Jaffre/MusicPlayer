package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.repositories.interfaces.IPlaylistRepository
import fr.arthur.musicplayer.room.DAO.PlaylistDAO

class PlaylistRepository(
    private val playlistDao: PlaylistDAO
) : IPlaylistRepository {

    override suspend fun getAllPlayLists(): List<Playlist> {
        return playlistDao.getAll().map {
            Playlist(id = it.id, name = it.name, numberOfMusics = it.numberOfMusics)
        }
    }
}