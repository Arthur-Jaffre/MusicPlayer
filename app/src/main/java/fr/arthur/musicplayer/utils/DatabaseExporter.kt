package fr.arthur.musicplayer.utils

import android.content.Context
import android.widget.Toast
import fr.arthur.musicplayer.helpers.AppConstants.DB_NAME
import java.io.File

object DatabaseExporter {
    fun exportDatabaseWithWAL(context: Context) {
        val sourceDb = context.getDatabasePath(DB_NAME)
        val sourceWal = File(sourceDb.parentFile, "$DB_NAME-wal")
        val sourceShm = File(sourceDb.parentFile, "$DB_NAME-shm")
        val destDir = context.getExternalFilesDir(null)!!

        try {
            sourceDb.copyTo(File(destDir, DB_NAME), overwrite = true)
            if (sourceWal.exists()) sourceWal.copyTo(
                File(destDir, "${DB_NAME}-wal"),
                overwrite = true
            )
            if (sourceShm.exists()) sourceShm.copyTo(
                File(destDir, "${DB_NAME}-shm"),
                overwrite = true
            )

            Toast.makeText(context, "Export OK", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Export échoué : ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}