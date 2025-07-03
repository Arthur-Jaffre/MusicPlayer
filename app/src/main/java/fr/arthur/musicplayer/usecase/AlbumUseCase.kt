package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.repositories.interfaces.IAlbumRepository
import fr.arthur.musicplayer.repositories.interfaces.IArtistRepository
import fr.arthur.musicplayer.repositories.interfaces.IMusicRepository

class AlbumUseCase(
    private val albumRepository: IAlbumRepository,
    private val musicRepository: IMusicRepository,
    private val artistRepository: IArtistRepository,
) {
    suspend fun getAlbumsByArtist(artistId: String): List<Album> =
        albumRepository.getAlbumsByArtist(artistId)

    suspend fun getAllAlbums(): List<Album> = albumRepository.getAllAlbums()

    suspend fun updateAlbum(album: Album, oldArtist: String) {
        // vérifier si l'artiste existe
        if (artistRepository.getArtistById(album.artistId) == null) {
            artistRepository.insertArtist(album.artistId, null)
        }

        // mettre à jour l'album
        albumRepository.updateAlbum(album)

        // récupérer les musiques de l'album à partir de l'ID de l'album
        val allMusics = musicRepository.getMusicsFromAlbum(album.id)
        for (music in allMusics) {
            // mettre à jour l'image, et le nom de l'artiste
            val updatedMusic = music.copy(
                imageUri = album.imageUri,
                artistIds = music.artistIds.filterNot { it == oldArtist } + album.artistId,
            )
            musicRepository.updateMusic(updatedMusic, album.name)
        }

        // si l'ancien artiste n'a plus de musiques, on le supprime
        val allMusicsByOldArtist = musicRepository.getMusicsByArtist(oldArtist)
        if (allMusicsByOldArtist.isEmpty()) {
            artistRepository.deleteArtistById(oldArtist)
        }
    }

    suspend fun getAlbumById(albumId: String): Album =
        albumRepository.getAlbumById(albumId)
}