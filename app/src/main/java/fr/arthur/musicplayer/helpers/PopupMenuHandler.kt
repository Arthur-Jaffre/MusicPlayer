package fr.arthur.musicplayer.helpers

import android.content.Intent
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.views.activities.EditMusicActivity
import fr.arthur.musicplayer.views.dialogs.PlayListDialog
import kotlinx.coroutines.launch

object PopupMenuHandler {

    fun show(
        anchor: View,
        owner: FragmentActivity,
        music: Music,
        playlistViewModel: PlayListListViewModel,
        onArtistClicked: suspend (String) -> Unit
    ) {
        val popup = PopupMenu(owner, anchor)
        popup.menuInflater.inflate(R.menu.music_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_artist -> {
                    owner.lifecycleScope.launch {
                        onArtistClicked(music.artistIds.first())
                    }
                    true
                }

                R.id.menu_add_to_playlist -> {
                    PlayListDialog(music.id, owner, playlistViewModel).show()
                    true
                }

                R.id.menu_edit_tags -> {
                    val intent = Intent(owner, EditMusicActivity::class.java).apply {
                        putExtra("music", music)
                    }
                    owner.startActivity(intent)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }
}

