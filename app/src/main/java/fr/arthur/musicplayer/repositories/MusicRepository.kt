package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.helpers.MusicScanner
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.room.DAO.MusicDAO
import fr.arthur.musicplayer.room.MusicEntity

class MusicRepository(
    private val scanner: MusicScanner,
    private val dao: MusicDAO
) : IMusicRepository {

    suspend fun loadCachedMusics(): List<Music> {
        return dao.getAll().map {
            Music(
                id = it.id,
                title = it.title,
                artist = Artist("0", it.artistName),
                duration = it.duration
            )
        }
    }

    suspend fun scanAndSaveMusics(
        onMusicFound: (Music) -> Unit,
        onComplete: () -> Unit
    ) {
        val scannedBuffer = mutableListOf<MusicEntity>()

        // Attendre la fin du scan avant de continuer
        scanner.scanAudioFilesSuspend { music ->
            onMusicFound(music)
            scannedBuffer.add(
                MusicEntity(
                    id = music.id,
                    title = music.title,
                    artistName = music.artist.name,
                    duration = music.duration
                )
            )
        }

        // Récupérer les données en cache dans Room
        val cached = dao.getAll()
        val cachedMap = cached.associateBy { it.id }
        val scannedMap = scannedBuffer.associateBy { it.id }

        // Calculer les ajouts/mises à jour
        val toInsertOrUpdate = scannedBuffer.filter { track ->
            cachedMap[track.id] == null || cachedMap[track.id] != track
        }

        // Calculer les suppressions
        val toDelete = cached.filter { track ->
            !scannedMap.containsKey(track.id)
        }

        // Appliquer les modifications en base uniquement si nécessaire
        if (toInsertOrUpdate.isNotEmpty() || toDelete.isNotEmpty()) {
            if (toDelete.isNotEmpty()) {
                dao.delete(toDelete)
            }
            if (toInsertOrUpdate.isNotEmpty()) {
                dao.insertAll(toInsertOrUpdate)
            }
        }

        onComplete()
    }

    override fun getAllArtists(): List<Artist> {
        TODO("Not yet implemented")
    }

    override fun getAllPlayLists(): List<Playlist> {
        TODO("Not yet implemented")
    }
}

