package fr.arthur.musicplayer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.viewHolder.AlbumViewHolder
import fr.arthur.musicplayer.models.Album

class AlbumAdapter(
    var onAlbumClick: ((Album) -> Unit)? = null
) : RecyclerView.Adapter<AlbumViewHolder>() {
    private val items = mutableListOf<Album>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<Album>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = items[position]
        holder.bind(album)

        holder.itemView.setOnClickListener {
            // Navigation vers la page de l'album
            onAlbumClick?.invoke(album)
        }
    }

    override fun getItemCount(): Int = items.size
}
