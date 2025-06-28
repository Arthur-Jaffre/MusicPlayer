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
import fr.arthur.musicplayer.adapters.MusicAdapter
import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.views.navigation.navigateToArtistOverview
import org.koin.android.ext.android.inject

class AlbumOverviewFragment : Fragment() {

    private val musicViewModel: MusicListViewModel by inject()
    private val artistViewModel: ArtistListViewModel by inject()
    private lateinit var adapter: MusicAdapter
    private lateinit var album: Album

    companion object {
        fun newInstance(album: Album): AlbumOverviewFragment {
            return AlbumOverviewFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("album", album)
                }
            }
        }
    }

    private fun extractAlbumFromArguments(): Album {
        return requireArguments().getSerializable("album") as Album
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playlists, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        album = extractAlbumFromArguments()

        adapter = MusicAdapter(
            toFavorites = { music -> musicViewModel.toFavorites(music) },
            onArtistClick = { artistId ->
                artistViewModel.getArtistById(artistId)
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        musicViewModel.musicsObservable.observe {
            adapter.submitList(it)
            view.findViewById<TextView>(R.id.subtitle).text =
                getString(R.string.playlist_number_of_musics_count, it.size)
        }

        artistViewModel.artistEvent.observe(viewLifecycleOwner) { event ->
            event.getIfNotHandled()?.let { artist ->
                navigateToArtistOverview(artist)
            }
        }

        getMusicsFromAlbum()
        setupToolbar(view)

        return view
    }

    private fun setupToolbar(view: View) {
        view.findViewById<TextView>(R.id.title).text = album.name

        val imageView = view.findViewById<ImageView>(R.id.icon)
        Glide.with(this)
            .load(album.imageUri)
            .placeholder(R.drawable.ic_default_artist)
            .error(R.drawable.ic_default_album)
            .into(imageView)
    }

    private fun getMusicsFromAlbum() {
        musicViewModel.getMusicsByAlbum(album)
    }
}
