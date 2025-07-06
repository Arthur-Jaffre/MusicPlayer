package fr.arthur.musicplayer.manager

import android.content.Context
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.models.PlayerState

object PlayerManager {

    private var queue: List<Music> = emptyList()
    private var currentIndex: Int = -1

    private var exoPlayer: ExoPlayer? = null

    private val observers = mutableListOf<(PlayerState) -> Unit>()

    fun init(context: Context) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    notifyObservers()
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    currentIndex = exoPlayer?.currentMediaItemIndex ?: -1
                    notifyObservers()
                }
            })
        }
    }

    fun ensureServiceRunning(context: Context) {
        context.startForegroundService(Intent(context, MusicPlayerService::class.java))
    }

    fun playQueue(startIndex: Int, newQueue: List<Music>, context: Context) {
        queue = newQueue
        currentIndex = startIndex

        val mediaItems = queue.map { music ->
            MediaItem.Builder()
                .setUri(music.id)
                .setMediaId(music.id)
                .setTag(music)
                .build()
        }

        ensureServiceRunning(context)
        exoPlayer?.setMediaItems(mediaItems, startIndex, 0L)
        exoPlayer?.prepare()
        exoPlayer?.play()
        notifyObservers()
    }

    fun togglePlayPause() {
        if (exoPlayer?.isPlaying == true) {
            exoPlayer?.pause()
        } else {
            exoPlayer?.play()
        }
        notifyObservers()
    }

    fun next() {
        val size = getQueueSize()
        if (size == 0) return

        val nextIndex = if (currentIndex + 1 >= size) 0 else currentIndex + 1
        currentIndex = nextIndex
        exoPlayer?.seekTo(nextIndex, 0L)
        notifyObservers()
    }

    fun previous() {
        val size = getQueueSize()
        if (size == 0) return

        val prevIndex = if (currentIndex - 1 < 0) size - 1 else currentIndex - 1
        currentIndex = prevIndex
        exoPlayer?.seekTo(prevIndex, 0L)
        notifyObservers()
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
    }

    fun currentMusic(): Music? {
        val tag = exoPlayer?.currentMediaItem?.localConfiguration?.tag
        return tag as? Music
    }

    fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying == true
    }

    fun getCurrentPosition(): Long {
        return exoPlayer?.currentPosition ?: 0
    }

    fun getDuration(): Long {
        return exoPlayer?.duration ?: 0
    }

    fun register(observer: (PlayerState) -> Unit) {
        observers.add(observer)
        observer(buildState())
    }

    fun unregister(observer: (PlayerState) -> Unit) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        val state = buildState()
        observers.forEach { it(state) }
    }

    fun getCurrentIndex(): Int {
        return currentIndex
    }

    fun getQueueSize(): Int {
        return queue.size
    }

    private fun buildState(): PlayerState {
        return PlayerState(
            music = currentMusic(),
            isPlaying = isPlaying(),
            position = getCurrentPosition(),
            duration = getDuration()
        )
    }
}
