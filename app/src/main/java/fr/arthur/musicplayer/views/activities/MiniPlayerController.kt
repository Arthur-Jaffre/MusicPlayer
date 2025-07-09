package fr.arthur.musicplayer.views.activities

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.manager.PlayerManager
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.models.PlayerState

class MiniPlayerController(
    private val context: Context,
    private val rootView: View
) {

    private val miniPlayer: View = rootView
    private val imageView: ImageView = rootView.findViewById(R.id.miniPlayerImage)
    private val titleView: TextView = rootView.findViewById(R.id.miniPlayerTitle)
    private val playPauseButton: ImageButton = rootView.findViewById(R.id.miniPlayerPlayPause)
    private val nextButton: ImageButton = rootView.findViewById(R.id.miniPlayerNext)
    private val prevButton: ImageButton = rootView.findViewById(R.id.miniPlayerPrev)

    private var currentMusic: Music? = null

    private val observer: (PlayerState) -> Unit = { state -> updateUI(state) }

    init {
        setupListeners()
        PlayerManager.register(observer)
    }

    private fun setupListeners() {
        miniPlayer.setOnClickListener {
            currentMusic?.let { music ->
                val intent = Intent(context, PlayerActivity::class.java)
                intent.putExtra("music", music)
                context.startActivity(intent)
            }
        }

        playPauseButton.setOnClickListener {
            PlayerManager.togglePlayPause()
        }

        nextButton.setOnClickListener {
            PlayerManager.next()
        }

        prevButton.setOnClickListener {
            PlayerManager.previous()
        }
    }

    private fun updateUI(state: PlayerState) {
        val music = state.music
        if (music == null) {
            miniPlayer.visibility = View.GONE
            currentMusic = null
        } else {
            currentMusic = music
            miniPlayer.visibility = View.VISIBLE
            titleView.text = music.title

            Glide.with(context)
                .load(music.imageUri)
                .placeholder(R.drawable.ic_waveform)
                .into(imageView)

            val icon = if (state.isPlaying) {
                android.R.drawable.ic_media_pause
            } else {
                android.R.drawable.ic_media_play
            }
            playPauseButton.setImageResource(icon)
        }
    }

    fun destroy() {
        PlayerManager.unregister(observer)
    }
}
