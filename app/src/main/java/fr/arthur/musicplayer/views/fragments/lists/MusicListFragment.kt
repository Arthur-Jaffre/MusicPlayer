package fr.arthur.musicplayer.views.fragments.lists

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.views.fragments.BaseFragment
import fr.arthur.musicplayer.views.navigation.navigateToArtistOverview
import org.koin.android.ext.android.inject

class MusicListFragment : BaseFragment() {

    override val layoutResId: Int = R.layout.fragment_list
    override val musicViewModel: MusicListViewModel by inject()
    override val artistViewModel: ArtistListViewModel by inject()
    override val playlistViewModel: PlayListListViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)


        recyclerView.adapter = musicAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        musicViewModel.musicsObservable.observe {
            musicAdapter.submitList(it.toList()) // Une ptn de journée perdu pour cette ligne =)
        }

        artistViewModel.artistEvent.observe(viewLifecycleOwner) { event ->
            event.getIfNotHandled()?.let { artist ->
                navigateToArtistOverview(artist)
            }
        }

        refreshMusics()
    }


    fun refreshMusics() {
        musicViewModel.loadMusics()
    }
}
