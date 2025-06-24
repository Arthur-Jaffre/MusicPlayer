package fr.arthur.musicplayer.repositories

import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.models.Playlist

class FakeMusicRepository : IMusicRepository {
    override fun getAllMusics(): List<Music> = listOf(
        Music("1", "Time", Artist("1", "Hans Zimmer"), 300),
        Music("2", "Dream On", Artist("2", "Aerosmith"), 275),
        Music("3", "Bohemian Rhapsody", Artist("3", "Queen"), 355)
    ).sortedBy { it.title.lowercase() }

    override fun getAllArtists(): List<Artist> = listOf(
        Artist("1", "Hans Zimmer"),
        Artist("2", "Aerosmith"),
        Artist("3", "Queen")
    ).sortedBy { it.name.lowercase() }

    override fun getAllPlayLists(): List<Playlist> = listOf(
        Playlist("1", "Summer", 2),
        Playlist("2", "Chill", 3)
    ).sortedBy { it.name.lowercase() }
}