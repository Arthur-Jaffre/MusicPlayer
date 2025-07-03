package fr.arthur.musicplayer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.viewHolder.PlaylistViewHolder
import fr.arthur.musicplayer.models.Playlist

class PlayListAdapter(
    var onPlayListClick: ((Playlist) -> Unit)? = null
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    private val items = mutableListOf<Playlist>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<Playlist>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = items[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onPlayListClick?.invoke(playlist)
        }
    }

    override fun getItemCount(): Int = items.size
}
