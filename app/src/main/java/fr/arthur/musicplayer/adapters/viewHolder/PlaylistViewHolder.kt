package fr.arthur.musicplayer.adapters.viewHolder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Playlist
import fr.arthur.musicplayer.models.enums.PlaylistAction

class PlaylistViewHolder(
    val view: View,
    val context: Context,
    val onMenuAction: ((Playlist, PlaylistAction) -> Unit)?
) : RecyclerView.ViewHolder(view) {
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
            val popup = PopupMenu(context, view)
            popup.menuInflater.inflate(R.menu.playlist_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_playlist -> {
                        onMenuAction?.invoke(playlist, PlaylistAction.EDIT)
                        true
                    }

                    R.id.delete_playlist -> {
                        onMenuAction?.invoke(playlist, PlaylistAction.DELETE)
                        true
                    }

                    else -> false
                }
            }

            popup.show()
        }
    }
}
