package fr.arthur.musicplayer.usecase

import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.repositories.interfaces.IAlbumRepository
import fr.arthur.musicplayer.repositories.interfaces.IArtistRepository
import fr.arthur.musicplayer.repositories.interfaces.IMusicRepository
import fr.arthur.musicplayer.room.entities.ArtistEntity
import java.util.UUID

class MusicUseCase(
    private val musicRepository: IMusicRepository,
    private val artistRepository: IArtistRepository,
    private val albumRepository: IAlbumRepository
) {

    suspend fun loadCachedMusics(): List<Music> {
        return musicRepository.loadCachedMusics()
    }

    suspend fun getMusicsByPlaylist(playlist: Playlist): List<Music> {
        return musicRepository.getMusicsByPlaylist(playlist.id)
    }

    suspend fun getArtistsById(artistIds: List<String>): List<ArtistEntity> {
        return artistRepository.getArtistsById(artistIds)
    }

    suspend fun insertArtist(artistIds: List<ArtistEntity>): List<ArtistEntity> {
        return artistIds.map { artistRepository.insertArtist(it.id, it.imageUri) }
    }

    suspend fun getAlbumsByName(albumName: String): List<Album> {
        return albumRepository.getAlbumsByName(albumName)
    }

    suspend fun createAlbum(albumName: String, artistId: String): Album {
        val newAlbum = Album(
            id = UUID.randomUUID().toString(),
            name = albumName,
            artistId = artistId,
            imageUri = null
        )
        albumRepository.addAlbum(newAlbum)
        return newAlbum
    }

    suspend fun updateMusic(music: Music) {
        val existingArtists =
            getArtistsById(music.artistIds) // récupère les artistes existants liés à la musique
        val missingIds =
            music.artistIds.filter { it -> it !in existingArtists.map { it.id } } // identifie les IDs absents (non dans la base)
        val createdArtists = insertArtist(
            missingIds.map { id ->
                ArtistEntity(
                    id = id,
                    imageUri = null
                )
            } // Crée et insère en base les artistes manquants
        )
        val allArtists =
            existingArtists + createdArtists // fusionne les artistes existants et nouvellement créés

        val matchingAlbums = getAlbumsByName(music.albumId)
            .filter { album -> allArtists.any { it.id == album.artistId } }

        val album = if (matchingAlbums.isNotEmpty()) {
            matchingAlbums.first()
        } else {
            createAlbum(music.albumId, allArtists.first().id)
        }

        musicRepository.updateMusic(
            music.copy(
                albumId = album.id,
                artistIds = allArtists.map { it.id }
            ),
            album.name
        )

        albumRepository.deleteOrphanAlbums()
        artistRepository.deleteOrphanArtists()
    }

    suspend fun getAllFavoritesMusics(): List<Music> {
        return musicRepository.getAllFavoritesMusics()
    }

    suspend fun getMusicsByAlbum(album: Album): List<Music> {
        return musicRepository.getMusicsFromAlbum(album.id)
    }

    suspend fun updateFavorites(music: Music) {
        musicRepository.updateFavorites(music)
    }

    suspend fun getRecentlyAdded(): List<Music> {
        return musicRepository.getRecentlyAdded()
    }

    suspend fun getMusicsByArtist(artist: Artist): List<Music> {
        return musicRepository.getMusicsByArtist(artist.id)
    }

}
