package fr.arthur.musicplayer.views.fragments.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.ArtistAdapter
import fr.arthur.musicplayer.helpers.AppConstants
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.views.fragments.overviews.ArtistOverviewFragment
import org.koin.android.ext.android.inject

class ArtistListFragment : Fragment() {

    private val viewModel: ArtistListViewModel by inject()
    private lateinit var adapter: ArtistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        setupRecyclerView(view)
        observeArtists()
        viewModel.loadArtists()
        return view
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), AppConstants.COLUMNS_NUMBER)
        adapter = ArtistAdapter()
        setupAdapterClickListener()
        recyclerView.adapter = adapter
    }

    private fun setupAdapterClickListener() {
        adapter.onArtistClick = { artist ->
            navigateToArtistOverview(artist)
        }
    }

    private fun observeArtists() {
        viewModel.artistsObservable.observe {
            adapter.submitList(it)
        }
    }

    private fun navigateToArtistOverview(artist: Artist) {
        val fragment = ArtistOverviewFragment.newInstance(artist)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}