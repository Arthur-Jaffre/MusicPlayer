package fr.arthur.musicplayer.views.navigation

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.views.fragments.lists.ArtistListFragment
import fr.arthur.musicplayer.views.fragments.lists.MusicListFragment
import fr.arthur.musicplayer.views.fragments.lists.PlaylistListFragment

class MainActivityFragmentNavigator(private val activity: AppCompatActivity) {

    fun showHomeMusics() {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MusicListFragment())
            .commit()
    }

    fun setup() {
        activity.findViewById<View>(R.id.menu_artists).setOnClickListener {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ArtistListFragment())
                .addToBackStack(null)
                .commit()
        }

        activity.findViewById<View>(R.id.menu_songs).setOnClickListener {
            showHomeMusics()
        }

        activity.findViewById<View>(R.id.menu_playlists).setOnClickListener {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlaylistListFragment())
                .addToBackStack(null)
                .commit()
        }
    }

}