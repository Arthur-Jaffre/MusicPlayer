package fr.arthur.musicplayer.manager

import android.app.Service
import android.content.Intent
import android.os.IBinder
import fr.arthur.musicplayer.models.PlayerState

class MusicPlayerService : Service() {

    private lateinit var notificationManager: MusicNotificationManager
    private lateinit var observer: (PlayerState) -> Unit

    override fun onCreate() {
        super.onCreate()

        notificationManager = MusicNotificationManager(this)
        notificationManager.createChannel()

        observer = { state ->
            val notif = notificationManager.buildNotification(state.music, state.isPlaying)
            startForeground(1, notif)
        }

        PlayerManager.register(observer)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val currentState = PlayerManager.buildState()
        val notification =
            notificationManager.buildNotification(currentState.music, currentState.isPlaying)
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayerManager.unregister(observer)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
