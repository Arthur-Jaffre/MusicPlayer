package fr.arthur.musicplayer.helpers

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PermissionHandler(
    private val activity: AppCompatActivity,
    private val folderUriStore: FolderUriStore,
    private val onGranted: () -> Unit
) {

    companion object {
        private const val REQUEST_CODE_AUDIO = 101
        private const val REQUEST_CODE_NOTIFICATION = 102
    }

    private val folderPickerLauncher: ActivityResultLauncher<Uri?> =
        activity.registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri != null) {
                activity.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                folderUriStore.save(uri)
                onGranted()
            }
        }

    fun hasAudioPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        } else {
            activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun requestAudioPermission() {
        if (!hasAudioPermission()) {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            activity.requestPermissions(arrayOf(permission), REQUEST_CODE_AUDIO)
        } else {
            // Audio déjà accordée, passer à notification
            requestNotificationPermission()
        }
    }

    fun requestNotificationPermission() {
        if (!hasNotificationPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_NOTIFICATION
                )
            } else {
                // Android < TIRAMISU, pas besoin de permission notification, passer au dossier
                handleFolderAccess()
            }
        } else {
            // Notification déjà accordée, passer au dossier
            handleFolderAccess()
        }
    }

    fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // Permission refusée, ne pas continuer
            return
        }

        when (requestCode) {
            REQUEST_CODE_AUDIO -> {
                // Audio accordée, demander notification
                requestNotificationPermission()
            }

            REQUEST_CODE_NOTIFICATION -> {
                // Notification accordée, gérer accès dossier
                handleFolderAccess()
            }
        }
    }

    private fun handleFolderAccess() {
        if (folderUriStore.get() == null) {
            StorageDialog(activity) { launchFolderPicker() }.show()
        } else {
            onGranted()
        }
    }

    fun launchFolderPicker() {
        folderPickerLauncher.launch(null)
    }
}
