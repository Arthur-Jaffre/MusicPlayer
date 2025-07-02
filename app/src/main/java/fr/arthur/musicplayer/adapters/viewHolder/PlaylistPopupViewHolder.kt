package fr.arthur.musicplayer.adapters.viewHolder

import android.content.Context
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Playlist

class PlaylistPopupViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {
    private val title = view.findViewById<TextView>(R.id.playlist_title)
    private val isChecked = view.findViewById<RadioButton>(R.id.check)

    fun bind(playlist: Playlist) {
        title.text = playlist.name

        if (isChecked.isChecked) {
            // TODO : ajouter à playlist
        }
    }
}
