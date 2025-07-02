package fr.arthur.musicplayer.usecase

import androidx.room.Transaction
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.repositories.interfaces.IAlbumRepository
import fr.arthur.musicplayer.repositories.interfaces.IArtistRepository
import fr.arthur.musicplayer.repositories.interfaces.IMusicRepository

class ArtistUseCase(
    private val artistRepository: IArtistRepository,
    private val musicRepository: IMusicRepository,
    private val albumRepository: IAlbumRepository
) {
    suspend fun loadArtists(): List<Artist> = artistRepository.getAllArtists()
    suspend fun getArtistById(id: String) = artistRepository.getArtistById(id)
    suspend fun getMusicsByArtistId(id: String) = musicRepository.getMusicsByArtist(id)
    suspend fun insertArtist(id: String, imageUri: String?) =
        artistRepository.insertArtist(id, imageUri)

    suspend fun deleteArtistById(id: String) = artistRepository.deleteArtistById(id)
    suspend fun updateArtist(artist: Artist) = artistRepository.updateArtist(artist)


    @Transaction
    suspend fun updateArtist(newArtist: Artist, oldArtistId: String) {
        if (newArtist.id != oldArtistId) {
            // ajouter le nouvel artiste
            insertArtist(newArtist.id, newArtist.imageUri)

            // récupérer et mettre à jour les musiques
            val allMusics = getMusicsByArtistId(oldArtistId)
            for (music in allMusics) {
                val updatedMusic = music.copy(
                    artistIds = music.artistIds.filterNot { it == oldArtistId } + newArtist.id
                )
                val album = albumRepository.getAlbumById(updatedMusic.albumId)
                musicRepository.updateMusic(updatedMusic, album.name)
            }

            // mettre à jour les albums
            val albums = albumRepository.getAlbumsByArtist(oldArtistId)
            for (album in albums) {
                albumRepository.updateAlbum(album.copy(artistId = newArtist.id))
            }

            // supprimer l'ancien artiste
            deleteArtistById(oldArtistId)
        } else {
            updateArtist(newArtist)
        }
    }
}