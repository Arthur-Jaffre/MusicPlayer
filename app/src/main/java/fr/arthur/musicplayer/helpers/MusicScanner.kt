package fr.arthur.musicplayer.helpers

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import fr.arthur.musicplayer.helpers.AppConstants.UNKNOWN_ITEM
import fr.arthur.musicplayer.models.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MusicScanner(private val context: Context, private val folderUriStore: FolderUriStore) {

    suspend fun scanAudioFilesSuspend(onMusicFound: (Music) -> Unit) =
        suspendCoroutine { cont ->
            this.scanAudioFiles(
                onMusicFound = onMusicFound,
                onComplete = { cont.resume(Unit) }
            )
        }


    private fun scanAudioFiles(onMusicFound: (Music) -> Unit, onComplete: () -> Unit) {
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
                    context.contentResolver.openFileDescriptor(file.uri, "r")?.use { it ->
                        mmr.setDataSource(it.fileDescriptor)

                        val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            ?: file.name ?: UNKNOWN_ITEM

                        val durationMs =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                                ?.toIntOrNull() ?: 0
                        val duration = durationMs / 1000

                        val artistIds =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                                ?.split(';', ',', '/', '&')
                                ?.map { it.trim() }
                                ?.filter { it.isNotEmpty() }
                                ?: listOf(UNKNOWN_ITEM)

                        val albumName =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                                ?.takeIf { it.isNotBlank() } ?: UNKNOWN_ITEM

                        val year = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)
                            ?.takeIf { it.length >= 4 }  // on prend que l'année
                            ?.substring(0, 4)
                            ?.toIntOrNull()

                        val trackNumber =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                                ?.toIntOrNull()

                        val pictureData = mmr.embeddedPicture
                        val imageUri = pictureData?.let {
                            val fileName = "cover_${System.currentTimeMillis()}.jpg"
                            val coverFile = File(context.cacheDir, fileName)
                            coverFile.writeBytes(it)
                            Uri.fromFile(coverFile)
                        }?.toString()


                        val music = Music(
                            id = file.uri.toString(),
                            title = title,
                            duration = duration,
                            year = year,
                            trackNumber = trackNumber,
                            artistIds = artistIds,
                            albumId = albumName,
                            imageUri = imageUri
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
