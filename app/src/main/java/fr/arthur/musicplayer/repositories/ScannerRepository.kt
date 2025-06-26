package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.helpers.AppConstants.UNKNOWN_ITEM
import fr.arthur.musicplayer.helpers.MusicScanner
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.repositories.interfaces.IScannerRepository
import fr.arthur.musicplayer.room.DAO.AlbumDAO
import fr.arthur.musicplayer.room.DAO.ArtistDAO
import fr.arthur.musicplayer.room.DAO.MusicDAO
import fr.arthur.musicplayer.room.entities.AlbumEntity
import fr.arthur.musicplayer.room.entities.ArtistEntity
import fr.arthur.musicplayer.room.entities.MusicEntity

class ScannerRepository(
    private val scanner: MusicScanner,
    private val musicDao: MusicDAO,
    private val artistDao: ArtistDAO,
    private val albumDao: AlbumDAO
) : IScannerRepository {

    override suspend fun scanAndSaveEverything(
        onMusicFound: (Music) -> Unit,
        onComplete: () -> Unit
    ) {
        val musicsBuffer = mutableListOf<MusicEntity>()
        val artistsBuffer = mutableMapOf<String, ArtistEntity>()
        val albumsBuffer = mutableMapOf<Pair<String, String>, AlbumEntity>()

        val albumKeyToId = mutableMapOf<Pair<String, String>, String>()

        // Récupère les albums déjà en base
        albumDao.getAll().forEach {
            if (!it.name.isNullOrBlank()) albumKeyToId[it.name to it.artistId] = it.id
        }

        // Récupère les musiques déjà en base, pour préserver les favoris
        val cachedMusic = musicDao.getAll()
        val cachedMap = cachedMusic.associateBy { it.id }

        // Scan du stockage interne
        scanner.scanAudioFilesSuspend { rawMusic ->

            val artistName = rawMusic.artistId.ifBlank { UNKNOWN_ITEM }
            val albumName = rawMusic.albumId.ifBlank { UNKNOWN_ITEM }

            if (!artistsBuffer.containsKey(artistName)) {
                artistsBuffer[artistName] = ArtistEntity(id = artistName)
            }

            val albumKey = albumName to artistName
            val albumId = albumKeyToId.getOrPut(albumKey) {
                val newId = java.util.UUID.randomUUID().toString()
                albumsBuffer[albumKey] =
                    AlbumEntity(id = newId, name = albumName, artistId = artistName)
                newId
            }

            val completeMusic = rawMusic.copy(artistId = artistName, albumId = albumId)

            // Préserve l'état
            val existing = cachedMap[completeMusic.id]
            val isFavorite = existing?.isFavorite == true
            val addedAt = existing?.addedAt ?: System.currentTimeMillis()

            val entity = MusicEntity(
                id = completeMusic.id,
                title = completeMusic.title,
                duration = completeMusic.duration,
                artistId = completeMusic.artistId,
                albumId = completeMusic.albumId,
                year = completeMusic.year,
                trackNumber = completeMusic.trackNumber,
                imageUri = completeMusic.imageUri,
                isFavorite = isFavorite,
                addedAt = addedAt
            )

            musicsBuffer.add(entity)

            onMusicFound(completeMusic)
        }

        // Insert artistes manquants
        val cachedArtists = artistDao.getAll()
        val toInsertArtist =
            artistsBuffer.values.filter { new -> cachedArtists.none { it.id == new.id } }
        if (toInsertArtist.isNotEmpty()) artistDao.insertAll(toInsertArtist)

        // Insert albums manquants
        val cachedAlbums = albumDao.getAll()
        val toInsertAlbum =
            albumsBuffer.values.filter { new -> cachedAlbums.none { it.id == new.id } }
        if (toInsertAlbum.isNotEmpty()) albumDao.insertAll(toInsertAlbum)

        // Calcul des différences avec le cache existant
        val scannedMap = musicsBuffer.associateBy { it.id }

        val toInsertMusic = musicsBuffer.filter { new ->
            val existing = cachedMap[new.id]
            existing == null || existing != new
        }

        val toDeleteMusic = cachedMap.values.filter { it.id !in scannedMap }

        // Mise à jour BD
        if (toInsertMusic.isNotEmpty()) musicDao.insertAll(toInsertMusic)
        if (toDeleteMusic.isNotEmpty()) musicDao.delete(toDeleteMusic)

        onComplete()
    }

}