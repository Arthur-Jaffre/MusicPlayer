package fr.arthur.musicplayer.views.navigation

import androidx.fragment.app.Fragment
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.views.fragments.overviews.ArtistOverviewFragment

fun Fragment.navigateToArtistOverview(artist: Artist) {
    val fragment = ArtistOverviewFragment.newInstance(artist)
    parentFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit()
}
