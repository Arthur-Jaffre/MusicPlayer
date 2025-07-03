package fr.arthur.musicplayer.views.fragments.overviews

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.AlbumAdapter
import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.viewModel.AlbumListViewModel
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.views.activities.EditArtistActivity
import fr.arthur.musicplayer.views.fragments.BaseFragment
import org.koin.android.ext.android.inject

class ArtistOverviewFragment : BaseFragment() {

    override val layoutResId: Int = R.layout.fragment_artist_overview
    override val musicViewModel: MusicListViewModel by inject()
    private val albumViewModel: AlbumListViewModel by inject()
    override val artistViewModel: ArtistListViewModel by inject()
    override val playlistViewModel: PlayListListViewModel by inject()

    private lateinit var artist: Artist
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artist = extractArtistFromArguments()

        setupToolbar(view)
        setupAlbumRecyclerView(view)
        setupMusicRecyclerView(view)

        observeAlbums()
        observeMusics(view)
        observeArtists(view, viewLifecycleOwner)

        loadAlbums()
        loadMusics()
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

        view.findViewById<ImageView>(R.id.ic_modify).setOnClickListener {
            // ouvrir la page de modification artiste
            val intent = Intent(requireContext(), EditArtistActivity::class.java)
            intent.putExtra("artist", artist)
            requireContext().startActivity(intent)
        }
    }

    private fun setupAlbumRecyclerView(view: View) {
        val albumRecyclerView = view.findViewById<RecyclerView>(R.id.album_recyclerView)
        albumAdapter = AlbumAdapter()
        setupAlbumAdapterClickListener()
        albumRecyclerView.adapter = albumAdapter
        albumRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupAlbumAdapterClickListener() {
        albumAdapter.onAlbumClick = { album ->
            // Naviguer vers la page de l'album
            navigateToAlbumOverview(album)
        }
    }

    private fun navigateToAlbumOverview(album: Album) {
        val fragment = AlbumOverviewFragment.newInstance(album)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupMusicRecyclerView(view: View) {
        val musicRecyclerView = view.findViewById<RecyclerView>(R.id.music_recyclerView)
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


    private fun observeArtists(view: View, lifecycleOwner: LifecycleOwner) {
        artistViewModel.artistEvent.observe(lifecycleOwner) { event ->
            val artist: Artist = event.getIfNotHandled() ?: return@observe
            view.findViewById<TextView>(R.id.title).text = artist.id
            if (artist.imageUri != null) {
                view.findViewById<ImageView>(R.id.icon).setImageURI(artist.imageUri.toUri())
            }
            musicViewModel.getMusicsByArtist(artist)
        }
    }

    private fun loadAlbums() {
        albumViewModel.getAlbumsByArtist(artist)
    }

    private fun loadMusics() {
        musicViewModel.getMusicsByArtist(artist)
    }
}
