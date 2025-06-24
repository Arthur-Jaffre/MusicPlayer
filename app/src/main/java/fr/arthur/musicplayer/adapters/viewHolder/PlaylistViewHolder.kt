package fr.arthur.musicplayer.adapters.viewHolder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Playlist

class PlaylistViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {
    private val title = view.findViewById<TextView>(R.id.title)
    private val numberMusics = view.findViewById<TextView>(R.id.number_musics)
    private val moreIcon = view.findViewById<ImageView>(R.id.icon_more)

    fun bind(playlist: Playlist) {
        title.text = playlist.name
        numberMusics.text = buildString {
            append(playlist.numberOfMusics.toString())
            append(context.getString(R.string.musics_number))
        }

        moreIcon.setOnClickListener {
            // TODO: Afficher menu contextuel plus tard
        }
    }
}
