package fr.arthur.musicplayer.views.fragments.overviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.AlbumAdapter
import fr.arthur.musicplayer.adapters.MusicAdapter
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.viewModel.AlbumListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import org.koin.android.ext.android.inject

class ArtistOverviewFragment : Fragment() {

    private val musicViewModel: MusicListViewModel by inject()
    private val albumViewModel: AlbumListViewModel by inject()

    private lateinit var artist: Artist
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var albumAdapter: AlbumAdapter

    companion object {
        fun newInstance(artist: Artist): ArtistOverviewFragment {
            return ArtistOverviewFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("artist", artist)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_artist_overview, container, false)
        artist = extractArtistFromArguments()

        setupToolbar(view)
        setupAlbumRecyclerView(view)
        setupMusicRecyclerView(view)

        observeAlbums()
        observeMusics(view)

        loadAlbums()
        loadMusics()

        return view
    }

    private fun extractArtistFromArguments(): Artist {
        return requireArguments().getSerializable("artist") as Artist
    }

    private fun setupToolbar(view: View) {
        view.findViewById<TextView>(R.id.title).text = artist.id

        val imageView = view.findViewById<ImageView>(R.id.icon)
        Glide.with(this)
            .load(artist.imageUri)
            .placeholder(R.drawable.ic_default_artist)
            .error(R.drawable.ic_default_artist)
            .into(imageView)
    }

    private fun setupAlbumRecyclerView(view: View) {
        val albumRecyclerView = view.findViewById<RecyclerView>(R.id.album_recyclerView)
        albumAdapter = AlbumAdapter()
        albumRecyclerView.adapter = albumAdapter
        albumRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupMusicRecyclerView(view: View) {
        val musicRecyclerView = view.findViewById<RecyclerView>(R.id.music_recyclerView)
        musicAdapter = MusicAdapter(
            toFavorites = { music -> musicViewModel.toFavorites(music) },
            isFavorite = false
        )
        musicRecyclerView.adapter = musicAdapter
        musicRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeAlbums() {
        albumViewModel.albumsObservable.observe {
            albumAdapter.submitList(it)
        }
    }

    private fun observeMusics(view: View) {
        musicViewModel.musicsObservable.observe {
            musicAdapter.submitList(it)
            view.findViewById<TextView>(R.id.subtitle).text =
                getString(R.string.playlist_number_of_musics_count, it.size)
        }
    }

    private fun loadAlbums() {
        albumViewModel.getAlbumsByArtist(artist)
    }

    private fun loadMusics() {
        musicViewModel.getMusicsByArtist(artist)
    }
}
