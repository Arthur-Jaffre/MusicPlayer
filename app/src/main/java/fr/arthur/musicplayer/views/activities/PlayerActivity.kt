package fr.arthur.musicplayer.views.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.manager.PlayerManager
import fr.arthur.musicplayer.models.PlayerState
import fr.arthur.musicplayer.viewModel.AlbumListViewModel
import org.koin.android.ext.android.inject

class PlayerActivity : AppCompatActivity() {

    private lateinit var imageCover: ImageView
    private lateinit var titleView: TextView
    private lateinit var artistView: TextView
    private lateinit var albumView: TextView
    private lateinit var positionView: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnShuffle: ImageButton
    private lateinit var btnRepeat: ImageButton
    private lateinit var btnFavorite: ImageButton
    private lateinit var btnMenu: ImageButton
    private val albumViewModel: AlbumListViewModel by inject()

    private val handler = Handler(Looper.getMainLooper())
    private var isUserSeeking = false

    private val observer: (PlayerState) -> Unit = { updateUI(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initViews()
        initVM()
        initListeners()
        PlayerManager.register(observer)
        startSeekBarUpdater()
    }

    override fun onDestroy() {
        PlayerManager.unregister(observer)
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun initViews() {
        imageCover = findViewById(R.id.imageCover)
        titleView = findViewById(R.id.musicTitle)
        artistView = findViewById(R.id.musicArtist)
        albumView = findViewById(R.id.musicAlbum)
        positionView = findViewById(R.id.positionInQueue)
        seekBar = findViewById(R.id.seekBar)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)
        btnShuffle = findViewById(R.id.btnShuffle)
        btnRepeat = findViewById(R.id.btnRepeat)
        btnFavorite = findViewById(R.id.btnFavorite)
        btnMenu = findViewById(R.id.btnMenu)
    }

    private fun initListeners() {
        btnPlayPause.setOnClickListener { PlayerManager.togglePlayPause() }
        btnPrevious.setOnClickListener { PlayerManager.previous() }
        btnNext.setOnClickListener { PlayerManager.next() }

        btnShuffle.setOnClickListener { onShuffleClicked() }
        btnRepeat.setOnClickListener { onRepeatClicked() }
        btnFavorite.setOnClickListener { onFavoriteClicked() }
        btnMenu.setOnClickListener { onMenuClicked() }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(sb: SeekBar?) {
                isUserSeeking = true
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
                sb?.progress?.toLong()?.let { PlayerManager.seekTo(it) }
                isUserSeeking = false
            }
        })
    }

    private fun initVM() {
        albumViewModel.albumObservable.observe {
            albumView.text = it.name
        }
    }

    private fun updateUI(state: PlayerState) {
        val music = state.music ?: return
        titleView.text = music.title
        artistView.text = music.artistIdsAsString()
        albumViewModel.getAlbumById(music.albumId)

        positionView.text =
            buildString {
                append(PlayerManager.getCurrentIndex() + 1)
                append("/")
                append(PlayerManager.getQueueSize())
            }

        Glide.with(this)
            .load(music.imageUri)
            .placeholder(R.drawable.ic_waveform)
            .into(imageCover)

        val icon = if (state.isPlaying) {
            android.R.drawable.ic_media_pause
        } else {
            android.R.drawable.ic_media_play
        }
        btnPlayPause.setImageResource(icon)
    }

    private fun startSeekBarUpdater() {
        handler.post(object : Runnable {
            override fun run() {
                if (!isUserSeeking) {
                    val pos = PlayerManager.getCurrentPosition()
                    val dur = PlayerManager.getDuration()
                    seekBar.max = dur.toInt()
                    seekBar.progress = pos.toInt()
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun onShuffleClicked() {
        // TODO: implémenter le mode shuffle
    }

    private fun onRepeatClicked() {
        // TODO: implémenter le mode repeat
    }

    private fun onFavoriteClicked() {
        // TODO: implémenter l'ajout aux favoris
    }

    private fun onMenuClicked() {
        // TODO: implémenter le menu contextuel
    }
}
