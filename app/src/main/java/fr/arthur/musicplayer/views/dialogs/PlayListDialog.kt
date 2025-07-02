package fr.arthur.musicplayer.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.PlayListPopupAdapter
import fr.arthur.musicplayer.viewModel.PlayListListViewModel

class PlayListDialog(
    context: Context,
    private val playlistViewModel: PlayListListViewModel
) : Dialog(context) {
    private lateinit var entryText: EditText
    private lateinit var addButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayListPopupAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_playlist_options)

        playlistViewModel.playlistObservable.observe {
            adapter.submitList(it)
        }

        setupComponents()
        setupRecyclerView()
        loadPlaylists()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PlayListPopupAdapter()
        recyclerView.adapter = adapter
    }

    private fun setupComponents() {
        entryText = findViewById(R.id.edit_text_input)
        addButton = findViewById(R.id.button_validate)
    }

    private fun loadPlaylists() {
        playlistViewModel.loadPlaylists()
    }

}