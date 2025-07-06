package fr.arthur.musicplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.viewHolder.MusicViewHolder
import fr.arthur.musicplayer.models.Music

class MusicAdapter(
    private val onShowOptions: (Context, Music) -> Unit,
    private val onMusicClick: (Music) -> Unit
) : ListAdapter<Music, MusicViewHolder>(MusicDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = getItem(position)
        holder.bind(music)
        
        holder.itemView.findViewById<ImageView>(R.id.icon_more).setOnClickListener {
            onShowOptions(it.context, music)
        }

        holder.itemView.setOnClickListener {
            onMusicClick(music)
        }
    }

    private class MusicDiffCallback : DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.duration == newItem.duration &&
                    oldItem.albumId == newItem.albumId &&
                    oldItem.isFavorite == newItem.isFavorite &&
                    oldItem.artistIdsAsString() == newItem.artistIdsAsString()
        }

    }
}
