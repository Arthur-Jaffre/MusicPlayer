package fr.arthur.musicplayer.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.arthur.musicplayer.adapters.MusicAdapter
import fr.arthur.musicplayer.helpers.MusicAdapterHandler
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel

abstract class BaseFragment : Fragment() {

    protected abstract val layoutResId: Int
    protected abstract val musicViewModel: MusicListViewModel
    protected abstract val playlistViewModel: PlayListListViewModel
    protected abstract val artistViewModel: ArtistListViewModel

    protected val musicAdapter: MusicAdapter by lazy {
        MusicAdapter { context, music ->
            MusicAdapterHandler(
                playlistViewModel = playlistViewModel,
                toFavorites = { musicViewModel.toFavorites(it) },
                onArtistClick = { artistId -> artistViewModel.getArtistById(artistId) }
            ).showOptions(context, music)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutResId, container, false)
    }
}
