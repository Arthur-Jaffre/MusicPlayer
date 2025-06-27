package fr.arthur.musicplayer.helpers

import android.content.Context
import androidx.core.net.toUri
import com.mpatric.mp3agic.ID3v24Tag
import com.mpatric.mp3agic.Mp3File
import fr.arthur.musicplayer.models.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AudioMetadataUpdater(private val context: Context) {
    suspend fun updateAudioMetadata(music: Music, albumName: String?) {
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            val audioUri = music.id.toUri()

            // Ouvrir le fichier en lecture seule pour copier dans un temporaire
            val tempFile = File.createTempFile("tempAudio", ".mp3", context.cacheDir)
            resolver.openInputStream(audioUri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Charger le MP3
            val mp3 = Mp3File(tempFile)

            // Obtenir ou créer un tag ID3v2.4
            val id3v2Tag = if (mp3.hasId3v2Tag()) {
                mp3.id3v2Tag
            } else {
                ID3v24Tag().also { mp3.id3v2Tag = it }
            }

            id3v2Tag.title = music.title ?: ""
            id3v2Tag.album = albumName ?: ""
            id3v2Tag.artist = music.artistIdsAsString()
            id3v2Tag.year = music.year?.toString() ?: ""
            id3v2Tag.track = music.trackNumber?.toString() ?: ""

            // Mettre la cover si disponible
            if (!music.imageUri.isNullOrEmpty()) {
                val imageUri = music.imageUri.toUri()
                val mimeType = resolver.getType(imageUri) ?: "image/jpeg"
                resolver.openInputStream(imageUri)?.use { imageStream ->
                    val imageBytes = imageStream.readBytes()
                    id3v2Tag.setAlbumImage(imageBytes, mimeType)
                }
            }

            // Sauvegarder dans un fichier temporaire modifié
            val tempModifiedFile =
                File.createTempFile("tempAudioModified", ".mp3", context.cacheDir)
            mp3.save(tempModifiedFile.absolutePath)

            // Écrire le fichier modifié dans l'URI SAF en écriture
            resolver.openFileDescriptor(audioUri, "rw")?.use { pfd ->
                FileInputStream(tempModifiedFile).use { input ->
                    FileOutputStream(pfd.fileDescriptor).use { output ->
                        input.copyTo(output)
                        output.fd.sync() // forcer flush disque
                    }
                }
            }

            // Nettoyer les fichiers temporaires
            tempFile.delete()
            tempModifiedFile.delete()
        }
    }
}