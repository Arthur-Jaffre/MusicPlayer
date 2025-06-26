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
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import org.koin.android.ext.android.inject

class MusicListFragment : Fragment() {

    private val viewModel: MusicListViewModel by inject()
    private lateinit var adapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        adapter = MusicAdapter(
            toFavorites = { music -> viewModel.toFavorites(music) },
            isFavorite = false
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.musicsObservable.observe {
            adapter.submitList(it)
        }

        refreshMusics()

        return view
    }

    fun refreshMusics() {
        viewModel.loadMusics()
    }
}