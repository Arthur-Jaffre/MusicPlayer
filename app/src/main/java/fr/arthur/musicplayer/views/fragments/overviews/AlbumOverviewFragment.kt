package fr.arthur.musicplayer.views.fragments.overviews

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.viewModel.AlbumListViewModel
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.views.activities.EditAlbumActivity
import fr.arthur.musicplayer.views.fragments.BaseFragment
import fr.arthur.musicplayer.views.navigation.navigateToArtistOverview
import org.koin.android.ext.android.inject

class AlbumOverviewFragment : BaseFragment() {

    override val layoutResId: Int = R.layout.fragment_playlists
    override val musicViewModel: MusicListViewModel by inject()
    override val artistViewModel: ArtistListViewModel by inject()
    override val playlistViewModel: PlayListListViewModel by inject()
    private val albumViewModel: AlbumListViewModel by inject()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        album = extractAlbumFromArguments()


        recyclerView.adapter = musicAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        musicViewModel.musicsObservable.observe {
            musicAdapter.submitList(it)
            view.findViewById<TextView>(R.id.subtitle).text =
                getString(R.string.playlist_number_of_musics_count, it.size)
        }

        artistViewModel.artistEvent.observe(viewLifecycleOwner) { event ->
            event.getIfNotHandled()?.let { artist ->
                navigateToArtistOverview(artist)
            }
        }

        albumViewModel.albumObservable.observe {
            view.findViewById<TextView>(R.id.title).text = it.name
            album = it
            Glide.with(this)
                .load(it.imageUri)
                .placeholder(R.drawable.ic_default_artist)
                .error(R.drawable.ic_default_album)
                .into(view.findViewById(R.id.icon))
        }

        getMusicsFromAlbum()
        setupToolbar(view)
    }

    private fun setupToolbar(view: View) {
        view.findViewById<TextView>(R.id.title).text = album.name

        val imageView = view.findViewById<ImageView>(R.id.icon)
        Glide.with(this)
            .load(album.imageUri)
            .placeholder(R.drawable.ic_default_artist)
            .error(R.drawable.ic_default_album)
            .into(imageView)


        view.findViewById<ImageView>(R.id.ic_modify).setOnClickListener {
            // ouvrir la page de modification artiste
            val intent = Intent(requireContext(), EditAlbumActivity::class.java)
            intent.putExtra("album", album)
            requireContext().startActivity(intent)
        }
    }

    private fun getMusicsFromAlbum() {
        musicViewModel.getMusicsByAlbum(album)
    }
}
