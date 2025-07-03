package fr.arthur.musicplayer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.viewHolder.PlaylistPopupViewHolder
import fr.arthur.musicplayer.models.Playlist

class PlayListPopupAdapter(
    private val onPlaylistChecked: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistPopupViewHolder>() {
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
    ): PlaylistPopupViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_popup, parent, false)
        return PlaylistPopupViewHolder(view, parent.context, onPlaylistChecked)
    }

    override fun onBindViewHolder(holder: PlaylistPopupViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}