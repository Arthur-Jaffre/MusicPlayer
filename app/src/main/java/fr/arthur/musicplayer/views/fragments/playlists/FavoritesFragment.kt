package fr.arthur.musicplayer.views.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.MusicAdapter
import fr.arthur.musicplayer.viewModel.FavoritesViewModel
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment() {
    private val viewModel: FavoritesViewModel by inject()
    private lateinit var adapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playlists, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        adapter = MusicAdapter(
            toFavorites = { music -> viewModel.toFavorites(music) },
            isFavorite = true
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.favoritesObservable.observe { favorites ->
            adapter.submitList(favorites)

            view.findViewById<TextView>(R.id.subtitle).text =
                getString(R.string.playlist_number_of_musics_count, favorites.size)

            if (favorites.isEmpty()) {
                Toast.makeText(requireContext(), R.string.no_music_found, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadFavorites()

        setupFragment(view)

        return view
    }

    private fun setupFragment(view: View) {
        view.findViewById<TextView>(R.id.title).text = getString(R.string.favoris_playlist)
        view.findViewById<ImageView>(R.id.icon).setImageResource(R.drawable.ic_favorite)
    }
}