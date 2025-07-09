package fr.arthur.musicplayer.manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.helpers.AppConstants.CHANNEL_ID
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.views.activities.PlayerActivity

class MusicNotificationManager(private val context: Context) {

    private val channelId = CHANNEL_ID

    fun createChannel() {
        val channel = NotificationChannel(
            channelId,
            context.getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = context.getString(R.string.channel_description)
        channel.setShowBadge(false)
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun buildNotification(music: Music?, isPlaying: Boolean): Notification {
        val playPauseIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, NotificationActionReceiver::class.java).setAction(
                NotificationActionReceiver.ACTION_PLAY_PAUSE
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
        val nextIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, NotificationActionReceiver::class.java).setAction(
                NotificationActionReceiver.ACTION_NEXT
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
        val prevIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, NotificationActionReceiver::class.java).setAction(
                NotificationActionReceiver.ACTION_PREV
            ),
            PendingIntent.FLAG_IMMUTABLE
        )


        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("music", music)

        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val playPauseIcon =
            if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(music?.title)
            .setContentText(music?.artistIdsAsString())
            .setSmallIcon(R.drawable.ic_waveform)
            .setContentIntent(contentIntent)
            .addAction(android.R.drawable.ic_media_previous, "Précédent", prevIntent)
            .addAction(playPauseIcon, if (isPlaying) "Pause" else "Lire", playPauseIntent)
            .addAction(android.R.drawable.ic_media_next, "Suivant", nextIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        return builder.build()
    }
}
