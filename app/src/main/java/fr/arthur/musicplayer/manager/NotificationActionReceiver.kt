package fr.arthur.musicplayer.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            ACTION_PLAY_PAUSE -> PlayerManager.togglePlayPause()
            ACTION_NEXT -> PlayerManager.next()
            ACTION_PREV -> PlayerManager.previous()
        }
    }

    companion object {
        const val ACTION_PLAY_PAUSE = "fr.arthur.musicplayer.ACTION_PLAY_PAUSE"
        const val ACTION_NEXT = "fr.arthur.musicplayer.ACTION_NEXT"
        const val ACTION_PREV = "fr.arthur.musicplayer.ACTION_PREV"
    }
}
