package fr.arthur.musicplayer.views.fragments.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.PlayListAdapter
import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.models.enums.PlaylistAction
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.views.dialogs.PlayListRenameDialog
import fr.arthur.musicplayer.views.fragments.playlists.FavoritesFragment
import fr.arthur.musicplayer.views.fragments.playlists.PlaylistsFragment
import fr.arthur.musicplayer.views.fragments.playlists.RecentlyAddedFragment
import org.koin.android.ext.android.inject

class PlaylistListFragment : Fragment() {

    private val viewModel: PlayListListViewModel by inject()
    private lateinit var adapter: PlayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playlist_page, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PlayListAdapter()
        setupPlaylistAdapterClickListener()
        recyclerView.adapter = adapter

        viewModel.playlistObservable.observe {
            adapter.submitList(it.toList())
        }

        setupPlaylistNames(view)
        viewModel.loadPlaylists()

        return view
    }

    private fun setupPlaylistAdapterClickListener() {
        adapter.onPlayListClick = { playlist ->
            // Naviguer vers la page de l'album
            navigateToPlayListOverview(playlist)
        }
        adapter.onMenuAction = { playlist, action ->
            when (action) {
                PlaylistAction.EDIT -> {
                    // ouvrir l'UI d'édition
                    PlayListRenameDialog(playlist, requireContext(), viewModel).show()
                }

                PlaylistAction.DELETE -> {
                    // Suppression playlist
                    viewModel.deletePlaylist(playlist)
                }
            }
        }
    }

    private fun navigateToPlayListOverview(playlist: Playlist) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PlaylistsFragment.newInstance(playlist))
            .addToBackStack(null)
            .commit()
    }


    private fun setupPlaylistNames(view: View) {
        val favoriteItem = view.findViewById<View>(R.id.favorite)
        favoriteItem.findViewById<ImageView>(R.id.icon_more).visibility = View.GONE
        val titleView = favoriteItem.findViewById<TextView>(R.id.title)
        titleView.text = getString(R.string.favoris_playlist)
        favoriteItem.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FavoritesFragment())
                .addToBackStack(null)
                .commit()
        }

        val recentlyAddedItem = view.findViewById<View>(R.id.recently_added)
        recentlyAddedItem.findViewById<ImageView>(R.id.icon_more).visibility = View.GONE
        val titleView2 = recentlyAddedItem.findViewById<TextView>(R.id.title)
        titleView2.text = getString(R.string.recently_added_playlist)
        recentlyAddedItem.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RecentlyAddedFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}