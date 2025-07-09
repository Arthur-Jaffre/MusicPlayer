package fr.arthur.musicplayer.adapters.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.helpers.AppConstants.TITLES_MAX_LENGTH
import fr.arthur.musicplayer.models.Music

class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title = view.findViewById<TextView>(R.id.title)
    private val artist = view.findViewById<TextView>(R.id.artist)

    private fun truncate(text: String, maxLength: Int = TITLES_MAX_LENGTH): String {
        return if (text.length > maxLength) {
            text.take(maxLength) + "…"
        } else {
            text
        }
    }

    fun bind(music: Music) {
        title.text = truncate(music.title.toString())
        artist.text = truncate(music.artistIdsAsString())
    }
}
