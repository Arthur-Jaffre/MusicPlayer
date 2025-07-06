package fr.arthur.musicplayer.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.ArtistAdapter
import fr.arthur.musicplayer.adapters.MusicAdapter
import fr.arthur.musicplayer.helpers.AppConstants
import fr.arthur.musicplayer.helpers.MusicAdapterHandler
import fr.arthur.musicplayer.manager.PlayerManager
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.viewModel.SearchViewModel
import org.koin.android.ext.android.inject

class SearchActivity : AppCompatActivity() {
    private lateinit var inputSearchBar: EditText
    private lateinit var artistRecyclerView: RecyclerView
    private lateinit var musicRecyclerView: RecyclerView
    private lateinit var keyboard: InputMethodManager
    private val searchViewModel: SearchViewModel by inject()
    private val playlistViewModel: PlayListListViewModel by inject()
    private val musicViewModel: MusicListViewModel by inject()
    private val artistViewModel: ArtistListViewModel by inject()
    private lateinit var artistAdapter: ArtistAdapter
    private lateinit var musicAdapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupComponents()
        setupSearchInput()
        initVM()
    }

    private fun initVM() {
        searchViewModel.artistsObservable.observe {
            artistAdapter.submitList(it)
        }
        searchViewModel.musicsObservable.observe {
            musicAdapter.submitList(it)
        }
    }

    private fun setupComponents() {
        artistAdapter = ArtistAdapter()
        artistAdapter.onArtistClick = { artist ->
            // Naviguer vers la page de l'artiste
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("artist", artist)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        val handler = MusicAdapterHandler(
            playlistViewModel = playlistViewModel,
            toFavorites = { musicViewModel.toFavorites(it) },
            onArtistClick = { artistId -> artistViewModel.getArtistById(artistId) },
            onMusicClick = { clickedMusic ->
                val list = musicAdapter.currentList
                PlayerManager.playQueue(
                    list.indexOfFirst {
                        it.id == clickedMusic.id
                    },
                    list,
                    this
                )
            }
        )

        musicAdapter = MusicAdapter(
            onShowOptions = { context, music ->
                handler.showOptions(context, music)
            },
            onMusicClick = { music ->
                handler.onMusicClicked(music)
            }
        )



        inputSearchBar = findViewById(R.id.search_bar)

        artistRecyclerView = findViewById(R.id.artist_list)
        artistRecyclerView.adapter = artistAdapter
        artistRecyclerView.layoutManager = GridLayoutManager(this, AppConstants.COLUMNS_NUMBER)

        musicRecyclerView = findViewById(R.id.music_list)
        musicRecyclerView.adapter = musicAdapter
        musicRecyclerView.layoutManager = LinearLayoutManager(this)

        keyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun setupSearchInput() {
        inputSearchBar.requestFocus()
        inputSearchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyboard.hideSoftInputFromWindow(inputSearchBar.windowToken, 0)
                val query = inputSearchBar.text.toString().trim()
                searchViewModel.searchByArtist(query)
                searchViewModel.searchByMusic(query)
                true
            } else {
                false
            }
        }
    }
}