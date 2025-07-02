package fr.arthur.musicplayer.helpers

import android.content.Context
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.views.dialogs.PlayListDialog

class PopupMusicActions {
    fun showAddToPlaylistDialog(context: Context, viewModel: PlayListListViewModel, music: Music) {
        PlayListDialog(context, viewModel).show()
    }
}