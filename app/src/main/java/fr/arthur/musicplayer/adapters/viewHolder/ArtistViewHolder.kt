package fr.arthur.musicplayer.adapters.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Artist

class ArtistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val artistImage = view.findViewById<ImageView>(R.id.imageArtist)
    private val artistName = view.findViewById<TextView>(R.id.textArtistName)

    fun bind(artist: Artist) {
        artistImage.setImageResource(R.drawable.ic_default_artist)
        artistName.text = artist.name
    }
}
