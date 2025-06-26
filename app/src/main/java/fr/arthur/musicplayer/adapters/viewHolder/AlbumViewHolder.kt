package fr.arthur.musicplayer.adapters.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Album

class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val albumImage = view.findViewById<ImageView>(R.id.imageAlbum)
    private val albumName = view.findViewById<TextView>(R.id.textAlbumName)

    fun bind(album: Album) {
        albumImage.setImageResource(R.drawable.ic_default_album)
        albumName.text = album.name
    }
}
