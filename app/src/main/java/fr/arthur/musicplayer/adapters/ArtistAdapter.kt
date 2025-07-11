package fr.arthur.musicplayer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.viewHolder.ArtistViewHolder
import fr.arthur.musicplayer.models.Artist

class ArtistAdapter(
    var onArtistClick: ((Artist) -> Unit)? = null
) : RecyclerView.Adapter<ArtistViewHolder>() {
    private val items = mutableListOf<Artist>()


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<Artist>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = items[position]
        holder.bind(artist)

        holder.itemView.setOnClickListener {
            // Navigation vers la page de l'artiste
            onArtistClick?.invoke(artist)
        }
    }

    override fun getItemCount(): Int = items.size
}
