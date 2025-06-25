package fr.arthur.musicplayer.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.ArtistAdapter
import fr.arthur.musicplayer.helpers.AppConstants.COLUMNS_NUMBER
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import kotlinx.coroutines.launch
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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), COLUMNS_NUMBER)

        adapter = ArtistAdapter()
        recyclerView.adapter = adapter

        viewModel.artistsObservable.observe {
            adapter.submitList(it)
        }

            viewModel.loadArtists()


        return view
    }
}