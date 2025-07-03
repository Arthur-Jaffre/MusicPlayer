package fr.arthur.musicplayer.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.viewModel.PlayListListViewModel

class PlayListRenameDialog(
    private val playlist: Playlist,
    context: Context,
    private val playlistViewModel: PlayListListViewModel
) : Dialog(context) {
    private lateinit var entryText: TextView
    private lateinit var addButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_rename_playlist)

        setupComponents()
        setupButton()
    }

    private fun setupButton() {
        addButton = findViewById(R.id.btn_confirm)
        addButton.setOnClickListener {
            val playlistName = entryText.text.toString()
            if (playlistName.isNotBlank()) {
                // Mettre à jour le nom de la playlist
                playlistViewModel.updatePlaylist(playlist.copy(name = playlistName))
                dismiss()
            }
        }
    }

    private fun setupComponents() {
        entryText = findViewById(R.id.edit_text_input)
        entryText.text = playlist.name
    }


}