package fr.arthur.musicplayer.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.PlayListPopupAdapter
import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import java.util.UUID

class PlayListDialog(
    private val musicId: String,
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

        setupComponents()
        setupRecyclerView()

        playlistViewModel.playlistObservable.observe {
            adapter.submitList(it)
        }

        loadPlaylists()
        setupButton()
    }

    private fun setupButton() {
        addButton = findViewById(R.id.button_validate)
        addButton.setOnClickListener {
            val playlistName = entryText.text.toString()
            if (playlistName.isNotBlank()) {
                val playlist = Playlist(
                    id = UUID.randomUUID().toString(),
                    name = playlistName,
                    numberOfMusics = 1
                )
                playlistViewModel.addPlaylist(playlist)
                playlistViewModel.insertMusic(playlist, musicId)
                dismiss()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PlayListPopupAdapter(
            onPlaylistChecked = { playlist ->
                playlistViewModel.insertMusic(
                    playlist,
                    musicId
                )
                playlistViewModel.updatePlaylist(
                    playlist.copy(numberOfMusics = playlist.numberOfMusics + 1)
                )
                this@PlayListDialog.dismiss()
                Toast.makeText(
                    context,
                    context.getString(R.string.added_to_playlist, playlist.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        recyclerView.adapter = adapter
    }

    private fun setupComponents() {
        entryText = findViewById(R.id.edit_text_input)
    }

    private fun loadPlaylists() {
        playlistViewModel.loadPlaylists()
    }

}