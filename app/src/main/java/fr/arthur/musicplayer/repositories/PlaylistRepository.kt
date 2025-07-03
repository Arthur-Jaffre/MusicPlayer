package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.repositories.interfaces.IPlaylistRepository
import fr.arthur.musicplayer.room.DAO.PlaylistDAO
import fr.arthur.musicplayer.room.entities.PlaylistEntity
import fr.arthur.musicplayer.room.entities.PlaylistMusicCrossRef

class PlaylistRepository(
    private val playlistDao: PlaylistDAO
) : IPlaylistRepository {

    override suspend fun getAllPlayLists(): List<Playlist> {
        return playlistDao.getAll().map {
            Playlist(id = it.id, name = it.name, numberOfMusics = it.numberOfMusics)
        }
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.insert(
            PlaylistEntity(
                id = playlist.id,
                name = playlist.name!!,
                numberOfMusics = playlist.numberOfMusics
            )
        )
    }

    override suspend fun insertMusic(playlistId: String, musicId: String) {
        playlistDao.insertMusic(
            PlaylistMusicCrossRef(
                playlistId = playlistId,
                musicId = musicId
            )
        )

    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.update(
            PlaylistEntity(
                id = playlist.id,
                name = playlist.name!!,
                numberOfMusics = playlist.numberOfMusics
            )
        )
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.delete(
            PlaylistEntity(
                id = playlist.id,
                name = playlist.name!!,
                numberOfMusics = playlist.numberOfMusics
            )
        )
    }

    override suspend fun deletePlaylistMusicCrossRef(playlistId: String) {
        playlistDao.deletePlaylistMusicCrossRef(playlistId)
    }
}