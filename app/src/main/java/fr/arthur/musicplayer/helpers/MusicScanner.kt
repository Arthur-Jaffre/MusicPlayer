package fr.arthur.musicplayer.helpers

import android.content.Context
import android.media.MediaMetadataRetriever
import androidx.documentfile.provider.DocumentFile
import fr.arthur.musicplayer.helpers.AppConstants.UNKNOWN_ITEM
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MusicScanner(private val context: Context, private val folderUriStore: FolderUriStore) {

    suspend fun scanAudioFilesSuspend(onMusicFound: (Music) -> Unit) =
        suspendCoroutine<Unit> { cont ->
            this.scanAudioFiles(
                onMusicFound = onMusicFound,
                onComplete = { cont.resume(Unit) }
            )
        }

    fun scanAudioFiles(onMusicFound: (Music) -> Unit, onComplete: () -> Unit) {
        val folderUri = folderUriStore.get() ?: run {
            onComplete()
            return
        }

        val root = DocumentFile.fromTreeUri(context, folderUri) ?: run {
            onComplete()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            scanRecursive(root, onMusicFound)
            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    private suspend fun scanRecursive(dir: DocumentFile, onMusicFound: (Music) -> Unit) {
        if (!dir.isDirectory) return

        dir.listFiles().forEach { file ->
            if (file.isDirectory) {
                scanRecursive(file, onMusicFound)
            } else if (file.isFile && file.type?.startsWith("audio/") == true) {
                val mmr = MediaMetadataRetriever()
                try {
                    context.contentResolver.openFileDescriptor(file.uri, "r")?.use {
                        mmr.setDataSource(it.fileDescriptor)
                        val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            ?: file.name ?: UNKNOWN_ITEM
                        val artistName =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                                ?: UNKNOWN_ITEM
                        val durationMs =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                                ?.toIntOrNull() ?: 0
                        val duration = durationMs / 1000
                        val artist = Artist("0", artistName)
                        val music = Music(
                            id = file.uri.toString(),
                            title = title,
                            artist = artist,
                            duration = duration
                        )
                        withContext(Dispatchers.Main) {
                            onMusicFound(music)
                        }
                    }
                } catch (_: Exception) {
                }
                mmr.release()
            }
        }
    }


}
