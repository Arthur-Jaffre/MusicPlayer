package fr.arthur.musicplayer.views.fragments.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.MusicAdapter
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.views.navigation.navigateToArtistOverview
import org.koin.android.ext.android.inject

class MusicListFragment : Fragment() {

    private val musicViewModel: MusicListViewModel by inject()
    private val artistViewModel: ArtistListViewModel by inject()
    private lateinit var adapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        adapter = MusicAdapter(
            toFavorites = { music -> musicViewModel.toFavorites(music) },
            isFavorite = false,
            onArtistClick = { artistId ->
                artistViewModel.getArtistById(artistId)
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        musicViewModel.musicsObservable.observe {
            adapter.submitList(it.toList()) // Une ptn de journée perdu pour cette ligne =)
        }

        artistViewModel.artistEvent.observe(viewLifecycleOwner) { event ->
            event.getIfNotHandled()?.let { artist ->
                navigateToArtistOverview(artist)
            }
        }

        refreshMusics()

        return view
    }


    fun refreshMusics() {
        musicViewModel.loadMusics()
    }
}
