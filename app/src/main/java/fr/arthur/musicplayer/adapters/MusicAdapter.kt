package fr.arthur.musicplayer.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.adapters.viewHolder.MusicViewHolder
import fr.arthur.musicplayer.models.Music

class MusicAdapter(
    private val onAddToFavorites: ((Music) -> Unit)? = null
) : RecyclerView.Adapter<MusicViewHolder>() {
    private val items = mutableListOf<Music>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<Music>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = items[position]
        holder.bind(music)

        holder.itemView.findViewById<ImageView>(R.id.icon_more).setOnClickListener {
            showOptionsPopup(it.context, music)
        }
    }

    override fun getItemCount(): Int = items.size

    private fun showOptionsPopup(context: Context, music: Music) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_music_options, null)
        val dialog = AlertDialog.Builder(context).setView(view).create()

        view.findViewById<Button>(R.id.btn_favoris).setOnClickListener {
            // Implémenter Ajouter aux favoris
            onAddToFavorites?.invoke(music)
            Toast.makeText(context, R.string.music_added_to_favorites, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btn_play_next).setOnClickListener {
            // Implémenter l'action Lire juste après
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btn_artist).setOnClickListener {
            // Implémenter l'action Artiste
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btn_add_to_playlist).setOnClickListener {
            // Implémenter Ajouter à la playlist
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btn_edit_tags).setOnClickListener {
            // Implémenter Éditer les tags
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btn_delete).setOnClickListener {
            // Implémenter Supprimer
            dialog.dismiss()
        }

        // Le bouton caché (retirer de la playlist)
        // view.findViewById<Button>(R.id.btn_remove_to_playlist).visibility = View.VISIBLE

        dialog.show()
    }
}

