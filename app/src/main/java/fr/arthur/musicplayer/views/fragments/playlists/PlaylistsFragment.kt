package fr.arthur.musicplayer.views.fragments.playlists

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.views.fragments.BaseFragment
import org.koin.android.ext.android.inject

class PlaylistsFragment : BaseFragment() {
    override val layoutResId: Int = R.layout.fragment_playlists
    override val artistViewModel: ArtistListViewModel by inject()
    override val playlistViewModel: PlayListListViewModel by inject()
    override val musicViewModel: MusicListViewModel by inject()
    private lateinit var playlist: Playlist

    companion object {
        fun newInstance(playlist: Playlist): PlaylistsFragment {
            return PlaylistsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("playlist", playlist)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = extractPlaylistFromArguments()
        setupRecyclerView(view)
        setupFragment(view)
        observePlaylists()
        musicViewModel.getMusicsByPlaylist(playlist)
    }

    private fun setupFragment(view: View) {
        view.findViewById<ImageView>(R.id.ic_modify).visibility = View.GONE
        view.findViewById<TextView>(R.id.title).text = playlist.name
        view.findViewById<ImageView>(R.id.icon).setImageResource(R.drawable.ic_playlist)
        view.findViewById<TextView>(R.id.subtitle).text =
            getString(R.string.playlist_number_of_musics_count, playlist.numberOfMusics)
    }

    private fun extractPlaylistFromArguments(): Playlist {
        return requireArguments().getSerializable("playlist") as Playlist
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = musicAdapter
    }

    private fun observePlaylists() {
        musicViewModel.musicsObservable.observe {
            musicAdapter.submitList(it)
        }
    }
}