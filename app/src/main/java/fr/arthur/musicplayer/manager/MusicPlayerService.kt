package fr.arthur.musicplayer.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import fr.arthur.musicplayer.R

class MusicPlayerService : Service() {

    override fun onCreate() {
        super.onCreate()
        // Rien ici
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "playback_channel"
        val channelName = "Lecture audio"

        val chan = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(chan)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Lecture en cours")
            .setContentText("Votre musique continue...")
            .setSmallIcon(R.drawable.ic_waveform) // Remplacez par votre icône
            .setOngoing(true)
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
