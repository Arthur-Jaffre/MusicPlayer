package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.helpers.AppConstants.UNKNOWN_ITEM
import fr.arthur.musicplayer.helpers.MusicScanner
import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.room.DAO.AlbumDAO
import fr.arthur.musicplayer.room.DAO.ArtistDAO
import fr.arthur.musicplayer.room.DAO.MusicDAO
import fr.arthur.musicplayer.room.DAO.PlaylistDAO
import fr.arthur.musicplayer.room.entities.AlbumEntity
import fr.arthur.musicplayer.room.entities.ArtistEntity
import fr.arthur.musicplayer.room.entities.MusicEntity

class MusicRepository(
    private val scanner: MusicScanner,
    private val musicDao: MusicDAO,
    private val artistDao: ArtistDAO,
    private val albumDao: AlbumDAO,
    private val playlistDao: PlaylistDAO
) : IMusicRepository {

    suspend fun loadCachedMusics(): List<Music> {
        return musicDao.getAll().map {
            Music(
                id = it.id,
                title = it.title,
                duration = it.duration,
                artistId = it.artistId,
                albumId = it.albumId,
                year = it.year,
                trackNumber = it.trackNumber,
                imageUri = it.imageUri,
                isFavorite = it.isFavorite
            )
        }
    }


    suspend fun scanAndSaveEverything(onMusicFound: (Music) -> Unit, onComplete: () -> Unit) {
        val musicsBuffer = mutableListOf<MusicEntity>()
        val artistsBuffer = mutableMapOf<String, ArtistEntity>()
        val albumsBuffer = mutableMapOf<Pair<String, String>, AlbumEntity>() // Pair(nom, artistId)

        val artistNameToId = mutableMapOf<String, String>()
        val albumKeyToId = mutableMapOf<Pair<String, String>, String>()

        // Précharger les artistes et albums existants
        artistDao.getAll().forEach {
            if (!it.name.isNullOrBlank()) artistNameToId[it.name] = it.id
        }
        albumDao.getAll().forEach {
            if (!it.name.isNullOrBlank()) albumKeyToId[it.name to it.artistId] = it.id
        }

        scanner.scanAudioFilesSuspend { rawMusic ->
            val artistName = rawMusic.artistId.ifBlank { UNKNOWN_ITEM }
            val albumName = rawMusic.albumId.ifBlank { UNKNOWN_ITEM }

            // ARTIST
            val artistId = artistNameToId.getOrPut(artistName) {
                val newId = java.util.UUID.randomUUID().toString()
                artistsBuffer[newId] = ArtistEntity(id = newId, name = artistName)
                newId
            }

            // ALBUM
            val albumKey = albumName to artistId
            val albumId = albumKeyToId.getOrPut(albumKey) {
                val newId = java.util.UUID.randomUUID().toString()
                albumsBuffer[albumKey] = AlbumEntity(id = newId, name = albumName, artistId = artistId)
                newId
            }

            // MUSIC
            val completeMusic = rawMusic.copy(artistId = artistId, albumId = albumId)
            musicsBuffer.add(
                MusicEntity(
                    id = completeMusic.id,
                    title = completeMusic.title,
                    duration = completeMusic.duration,
                    artistId = completeMusic.artistId,
                    albumId = completeMusic.albumId,
                    year = completeMusic.year,
                    trackNumber = completeMusic.trackNumber,
                    imageUri = completeMusic.imageUri,
                    isFavorite = completeMusic.isFavorite
                )
            )

            onMusicFound(completeMusic)
        }

        // ARTIST: insert new
        val cachedArtists = artistDao.getAll()
        val toInsertArtist = artistsBuffer.values.filter { new -> cachedArtists.none { it.id == new.id } }
        if (toInsertArtist.isNotEmpty()) artistDao.insertAll(toInsertArtist)

        // ALBUM: insert new
        val cachedAlbums = albumDao.getAll()
        val toInsertAlbum = albumsBuffer.values.filter { new -> cachedAlbums.none { it.id == new.id } }
        if (toInsertAlbum.isNotEmpty()) albumDao.insertAll(toInsertAlbum)

        // MUSIC: update si modif
        val cachedMusic = musicDao.getAll()
        val cachedMap = cachedMusic.associateBy { it.id }
        val scannedMap = musicsBuffer.associateBy { it.id }

        val toInsertMusic = musicsBuffer.filter { track ->
            cachedMap[track.id] == null || cachedMap[track.id] != track
        }
        val toDeleteMusic = cachedMusic.filter { it.id !in scannedMap }

        if (toInsertMusic.isNotEmpty()) musicDao.insertAll(toInsertMusic)
        if (toDeleteMusic.isNotEmpty()) musicDao.delete(toDeleteMusic)

        onComplete()
    }


    suspend fun getAllArtists(): List<Artist> {
        return artistDao.getAll().map {
            Artist(id = it.id, name = it.name)
        }
    }

    suspend fun getAllAlbums(): List<Album> {
        return albumDao.getAll().map {
            Album(id = it.id, name = it.name, artistId = it.artistId)
        }
    }

    suspend fun getAllPlayLists(): List<Playlist> {
        return playlistDao.getAll().map {
            Playlist(id = it.id, name = it.name, numberOfMusics = it.numberOfMusics)
        }
    }


    suspend fun getAlbumsFromArtist(artistId: String): List<Album> {
        return albumDao.getFromArtist(artistId).map {
            Album(id = it.id, name = it.name, artistId = it.artistId)
        }
    }

    suspend fun getMusicsFromAlbum(albumId: String): List<Music> {
        return musicDao.getFromAlbum(albumId).map {
            Music(
                id = it.id,
                title = it.title,
                duration = it.duration,
                artistId = it.artistId,
                albumId = it.albumId,
                year = it.year,
                trackNumber = it.trackNumber,
                imageUri = it.imageUri,
                isFavorite = it.isFavorite
            )
        }
    }
}
