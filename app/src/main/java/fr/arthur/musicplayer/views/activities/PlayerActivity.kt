package fr.arthur.musicplayer.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.helpers.PopupMenuHandler
import fr.arthur.musicplayer.manager.PlayerManager
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.models.PlayerState
import fr.arthur.musicplayer.usecase.ArtistUseCase
import fr.arthur.musicplayer.viewModel.AlbumListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PlayerActivity : AppCompatActivity() {

    private lateinit var imageCover: ImageView
    private lateinit var titleView: TextView
    private lateinit var artistView: TextView
    private lateinit var albumView: TextView
    private lateinit var positionView: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarLeftText: TextView
    private lateinit var seekBarRightText: TextView
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnShuffle: ImageButton
    private lateinit var btnRepeat: ImageButton
    private lateinit var btnFavorite: ImageButton
    private lateinit var btnMenu: ImageButton
    private lateinit var currentMusic: Music
    private val albumListViewModel: AlbumListViewModel by inject()
    private val artistUseCase: ArtistUseCase by inject()
    private val musicListViewModel: MusicListViewModel by inject()
    private val playlistViewModel: PlayListListViewModel by inject()
    private var isUserSeeking = false
    private var updateJob: Job? = null

    private val playerObserver: (PlayerState) -> Unit = { state -> updatePlayerState(state) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initViews()
        initVM()
        loadFromIntent()
        updateUI()
        initListeners()
        PlayerManager.register(playerObserver)

        startUpdatingSeekBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayerManager.unregister(playerObserver)
        updateJob?.cancel()
    }

    private fun initVM() {
        albumListViewModel.albumObservable.observe {
            albumView.text = it.name
        }
    }

    private fun loadFromIntent() {
        currentMusic = (intent.getSerializableExtra("music") as? Music)!!
    }

    private fun initViews() {
        imageCover = findViewById(R.id.imageCover)
        titleView = findViewById(R.id.musicTitle)
        artistView = findViewById(R.id.musicArtist)
        albumView = findViewById(R.id.musicAlbum)
        positionView = findViewById(R.id.positionInQueue)
        seekBar = findViewById(R.id.seekBar)
        seekBarLeftText = findViewById(R.id.seekBarLeftText)
        seekBarRightText = findViewById(R.id.seekBarRightText)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)
        btnShuffle = findViewById(R.id.btnShuffle)
        btnRepeat = findViewById(R.id.btnRepeat)
        btnFavorite = findViewById(R.id.btnFavorite)
        btnMenu = findViewById(R.id.btnMenu)
    }

    private fun updatePlayerState(state: PlayerState) {
        state.music?.let {
            currentMusic = it
            titleView.text = it.title
            artistView.text = it.artistIdsAsString()
            Glide.with(this)
                .load(it.imageUri)
                .placeholder(R.drawable.ic_waveform)
                .into(imageCover)
            updateFavoriteIcon()
            albumListViewModel.getAlbumById(state.music.albumId)
        }

        seekBar.max = state.duration.toInt()
        if (!isUserSeeking) {
            seekBar.progress = state.position.toInt()
            seekBarLeftText.text = formatMillisToTime(state.position)
        }
        seekBarRightText.text = formatMillisToTime(state.duration)

        btnPlayPause.setImageResource(
            if (state.isPlaying) {
                android.R.drawable.ic_media_pause
            } else {
                android.R.drawable.ic_media_play
            }
        )

        positionView.text = "${PlayerManager.getCurrentIndex() + 1}/${PlayerManager.getQueueSize()}"
        updateShuffleIcon()
        updateRepeatIcon()
    }

    private fun startUpdatingSeekBar() {
        updateJob = lifecycleScope.launch {
            while (isActive) {
                if (!isUserSeeking) {
                    val pos = PlayerManager.getCurrentPosition()
                    seekBar.progress = pos.toInt()
                    seekBarLeftText.text = formatMillisToTime(pos)
                }
                delay(1000)
            }
        }
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
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    seekBarLeftText.text = formatMillisToTime(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isUserSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isUserSeeking = false
                PlayerManager.seekTo(seekBar?.progress?.toLong() ?: 0L)
            }
        })
    }

    private fun updateUI() {
        titleView.text = currentMusic.title
        artistView.text = currentMusic.artistIdsAsString()
        albumListViewModel.getAlbumById(currentMusic.albumId)


        positionView.text = "${PlayerManager.getCurrentIndex() + 1}/${PlayerManager.getQueueSize()}"

        Glide.with(this)
            .load(currentMusic.imageUri)
            .placeholder(R.drawable.ic_waveform)
            .into(imageCover)

        updateFavoriteIcon()
        updateShuffleIcon()
        updateRepeatIcon()
    }

    @SuppressLint("DefaultLocale")
    private fun formatMillisToTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateShuffleIcon() {
        btnShuffle.setImageResource(
            if (PlayerManager.isShuffleMode()) {
                R.drawable.ic_shuffle
            } else {
                R.drawable.ic_unshuffle
            }
        )
    }

    private fun updateFavoriteIcon() {
        btnFavorite.setImageResource(
            if (currentMusic.isFavorite) {
                R.drawable.ic_heart
            } else {
                R.drawable.ic_unheart
            }
        )
    }

    private fun updateRepeatIcon() {
        btnRepeat.setImageResource(
            if (PlayerManager.isRepeatMode()) {
                R.drawable.ic_repeat
            } else {
                R.drawable.ic_unrepeat
            }
        )
    }

    private fun onShuffleClicked() {
        PlayerManager.toggleShuffle()
        updateShuffleIcon()
    }

    private fun onRepeatClicked() {
        PlayerManager.toggleRepeat()
        updateRepeatIcon()
    }

    private fun onFavoriteClicked() {
        currentMusic.isFavorite = !currentMusic.isFavorite
        updateFavoriteIcon()
        musicListViewModel.updateFavorites(currentMusic.copy())
    }

    private fun onMenuClicked() {
        PopupMenuHandler.show(
            anchor = btnFavorite,
            owner = this,
            music = currentMusic,
            playlistViewModel = playlistViewModel,
            onArtistClicked = { artistId ->
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("artist", artistUseCase.getArtistById(artistId)!!)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
            }
        )
    }

}
