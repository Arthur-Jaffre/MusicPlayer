package fr.arthur.musicplayer.views.fragments.playlists

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.viewModel.RecentlyAddedViewModel
import fr.arthur.musicplayer.views.fragments.BaseFragment
import fr.arthur.musicplayer.views.navigation.navigateToArtistOverview
import org.koin.android.ext.android.inject

class RecentlyAddedFragment : BaseFragment() {

    override val layoutResId: Int = R.layout.fragment_playlists
    private val recentlyAddedViewModel: RecentlyAddedViewModel by inject()
    override val artistViewModel: ArtistListViewModel by inject()
    override val playlistViewModel: PlayListListViewModel by inject()
    override val musicViewModel: MusicListViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.adapter = musicAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recentlyAddedViewModel.recentlyAddedObservable.observe { recent ->
            musicAdapter.submitList(recent)

            view.findViewById<TextView>(R.id.subtitle).text =
                getString(R.string.playlist_number_of_musics_count, recent.size)

            view.findViewById<ImageView>(R.id.ic_modify).visibility = View.GONE

            if (recent.isEmpty()) {
                Toast.makeText(requireContext(), R.string.no_music_found, Toast.LENGTH_SHORT).show()
            }
        }

        recentlyAddedViewModel.loadRecentlyAdded()

        artistViewModel.artistEvent.observe(viewLifecycleOwner) { event ->
            event.getIfNotHandled()?.let { artist ->
                navigateToArtistOverview(artist)
            }
        }

        setupFragment(view)
    }

    private fun setupFragment(view: View) {
        view.findViewById<TextView>(R.id.title).text = getString(R.string.recently_added_playlist)
        view.findViewById<ImageView>(R.id.icon).setImageResource(R.drawable.ic_recently)
    }
}