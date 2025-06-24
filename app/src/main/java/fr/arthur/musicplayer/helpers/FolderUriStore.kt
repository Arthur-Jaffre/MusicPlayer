package fr.arthur.musicplayer.helpers

import android.content.Context
import android.net.Uri
import androidx.core.content.edit
import androidx.core.net.toUri

class FolderUriStore(context: Context) {
    private val prefs = context.getSharedPreferences("music_app_prefs", Context.MODE_PRIVATE)

    fun save(uri: Uri) {
        prefs.edit { putString("folder_uri", uri.toString()) }
    }

    fun get(): Uri? {
        return prefs.getString("folder_uri", null)?.toUri()
    }
}
