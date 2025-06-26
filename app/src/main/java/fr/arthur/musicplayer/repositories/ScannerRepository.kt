package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.helpers.AppConstants.UNKNOWN_ITEM
import fr.arthur.musicplayer.helpers.MusicScanner
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.repositories.interfaces.IScannerRepository
import fr.arthur.musicplayer.room.DAO.AlbumDAO
import fr.arthur.musicplayer.room.DAO.ArtistDAO
import fr.arthur.musicplayer.room.DAO.MusicArtistDAO
import fr.arthur.musicplayer.room.DAO.MusicDAO
import fr.arthur.musicplayer.room.entities.AlbumEntity
import fr.arthur.musicplayer.room.entities.ArtistEntity
import fr.arthur.musicplayer.room.entities.MusicArtistCrossRef
import fr.arthur.musicplayer.room.entities.MusicEntity
import java.util.UUID

class ScannerRepository(
    private val scanner: MusicScanner,
    private val musicDao: MusicDAO,
    private val artistDao: ArtistDAO,
    private val albumDao: AlbumDAO,
    private val musicArtistDao: MusicArtistDAO
) : IScannerRepository {

    override suspend fun scanAndSaveEverything(
        onMusicFound: (Music) -> Unit,
        onComplete: () -> Unit
    ) {
        val musicsBuffer = mutableListOf<MusicEntity>()
        val musicArtistCrossRefs = mutableListOf<MusicArtistCrossRef>()
        val artistsBuffer = mutableMapOf<String, ArtistEntity>()
        val albumsBuffer = mutableMapOf<Pair<String, String>, AlbumEntity>()
        val albumKeyToId = mutableMapOf<Pair<String, String>, String>()

        albumDao.getAll().forEach {
            if (!it.name.isNullOrBlank()) albumKeyToId[it.name to it.artistId] = it.id
        }

        val cachedMusic = musicDao.getAll()
        val cachedMap = cachedMusic.associateBy { it.music.id }

        scanner.scanAudioFilesSuspend { rawMusic ->

            val albumName = rawMusic.albumId.ifBlank { UNKNOWN_ITEM }
            val artistNames = rawMusic.artistIds
                .flatMap { it.split(",", ";") }
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .ifEmpty { listOf(UNKNOWN_ITEM) }

            artistNames.forEach { name ->
                if (!artistsBuffer.containsKey(name)) {
                    artistsBuffer[name] = ArtistEntity(id = name)
                }
            }

            val albumKey = albumName to artistNames.first()
            val albumId = albumKeyToId.getOrPut(albumKey) {
                val newId = UUID.randomUUID().toString()
                albumsBuffer[albumKey] =
                    AlbumEntity(id = newId, name = albumName, artistId = artistNames.first())
                newId
            }

            val completeMusic = rawMusic.copy(
                albumId = albumId,
                artistIds = artistNames
            )

            val existing = cachedMap[completeMusic.id]
            val isFavorite = existing?.music?.isFavorite == true
            val addedAt = existing?.music?.addedAt ?: System.currentTimeMillis()

            val entity = MusicEntity(
                id = completeMusic.id,
                title = completeMusic.title,
                albumId = completeMusic.albumId,
                duration = completeMusic.duration,
                year = completeMusic.year,
                trackNumber = completeMusic.trackNumber,
                imageUri = completeMusic.imageUri,
                isFavorite = isFavorite,
                addedAt = addedAt
            )

            musicsBuffer.add(entity)

            artistNames.forEach { artistName ->
                musicArtistCrossRefs.add(
                    MusicArtistCrossRef(
                        musicId = entity.id,
                        artistId = artistName
                    )
                )
            }

            onMusicFound(completeMusic)
        }

        val cachedArtists = artistDao.getAll()
        val toInsertArtist =
            artistsBuffer.values.filter { new -> cachedArtists.none { it.id == new.id } }
        if (toInsertArtist.isNotEmpty()) artistDao.insertAll(toInsertArtist)

        val cachedAlbums = albumDao.getAll()
        val toInsertAlbum =
            albumsBuffer.values.filter { new -> cachedAlbums.none { it.id == new.id } }
        if (toInsertAlbum.isNotEmpty()) albumDao.insertAll(toInsertAlbum)

        val scannedMap = musicsBuffer.associateBy { it.id }

        val toInsertMusic = musicsBuffer.filter { new ->
            val existing = cachedMap[new.id]
            existing == null || existing != new
        }

        val toDeleteMusic = cachedMap.values.filter { it.music.id !in scannedMap }.map { it.music }

        if (toInsertMusic.isNotEmpty()) musicDao.insertAll(toInsertMusic)
        if (toDeleteMusic.isNotEmpty()) musicDao.delete(toDeleteMusic)

        musicArtistDao.deleteAll() // On réécrit complètement les relations
        if (musicArtistCrossRefs.isNotEmpty()) musicArtistDao.insertAll(musicArtistCrossRefs)

        albumDao.deleteOrphanAlbums()
        artistDao.deleteOrphanArtists()

        onComplete()
    }

}