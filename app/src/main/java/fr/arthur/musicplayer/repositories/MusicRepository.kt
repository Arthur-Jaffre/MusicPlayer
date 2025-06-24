package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.helpers.MusicScanner
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.models.Playlist

class MusicRepository(
    private val scanner: MusicScanner
) : IMusicRepository {

    fun scanAudioFiles(
        onMusicFound: (Music) -> Unit,
        onComplete: () -> Unit
    ) {
        scanner.scanAudioFiles(onMusicFound, onComplete)
    }
    
    override fun getAllArtists(): List<Artist> {
        TODO("Not yet implemented")
    }

    override fun getAllPlayLists(): List<Playlist> {
        TODO("Not yet implemented")
    }

}
