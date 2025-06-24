package fr.arthur.musicplayer.helpers

import android.content.Context
import android.media.MediaMetadataRetriever
import androidx.documentfile.provider.DocumentFile
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.models.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MusicScanner(private val context: Context, private val folderUriStore: FolderUriStore) {

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
                            ?: file.name ?: "Inconnu"
                        val artistName =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                                ?: "Inconnu"
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
