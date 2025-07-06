package fr.arthur.musicplayer.helpers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.views.activities.EditMusicActivity
import fr.arthur.musicplayer.views.dialogs.PlayListDialog

class MusicAdapterHandler(
    private val playlistViewModel: PlayListListViewModel,
    private val toFavorites: (Music) -> Unit,
    private val onArtistClick: ((String) -> Unit)?,
    private val onMusicClick: (Music) -> Unit
) {

    fun onMusicClicked(music: Music) {
        onMusicClick(music)
    }

    fun showOptions(context: Context, music: Music) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_music_options, null)
        val dialog = AlertDialog.Builder(context).setView(view).create()

        // ajouter aux favoris
        val favoriteBtn = view.findViewById<Button>(R.id.btn_favoris)
        favoriteBtn.text = if (music.isFavorite) context.getString(R.string.remove_from_favoris)
        else context.getString(R.string.add_to_favoris)
        favoriteBtn.setOnClickListener {
            toFavorites(music.copy(isFavorite = !music.isFavorite))
            Toast.makeText(context, R.string.updated_data, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        // voir artiste
        view.findViewById<Button>(R.id.btn_artist).apply {
            if (onArtistClick == null) visibility = View.GONE
            else setOnClickListener {
                onArtistClick.invoke(music.artistIds.first())
                dialog.dismiss()
            }
        }

        // ajouter à une playlist
        view.findViewById<Button>(R.id.btn_add_to_playlist).setOnClickListener {
            PlayListDialog(music.id, context, playlistViewModel).show()
            dialog.dismiss()
        }

        // éditer les tags
        view.findViewById<Button>(R.id.btn_edit_tags).setOnClickListener {
            val intent = Intent(context, EditMusicActivity::class.java)
            intent.putExtra("music", music)
            context.startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }
}
