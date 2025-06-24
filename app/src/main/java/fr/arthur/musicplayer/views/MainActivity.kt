package fr.arthur.musicplayer.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.helpers.FolderUriStore
import fr.arthur.musicplayer.helpers.appModule
import fr.arthur.musicplayer.views.fragments.ArtistListFragment
import fr.arthur.musicplayer.views.fragments.MusicListFragment
import fr.arthur.musicplayer.views.fragments.PlaylistListFragment
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class MainActivity : AppCompatActivity() {

    private lateinit var folderUriStore: FolderUriStore

    private val folderPickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                folderUriStore.save(uri)
                refreshMusics()
            }
        }

    private fun showFolderExplanationPopup() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_pick_folder_info, null)
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<View>(R.id.btn_continue).setOnClickListener {
            dialog.dismiss()
            launchFolderPicker()
        }

        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        folderUriStore = FolderUriStore(this)

        if (!hasAudioPermission()) {
            requestAudioPermission()
        } else {
            if (folderUriStore.get() == null) {
                showFolderExplanationPopup()
            } else {
                refreshMusics()
            }
        }

        showHomeMusics()
        setupFragments()
    }

    private fun hasAudioPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        } else {
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_AUDIO), 101)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (folderUriStore.get() == null) {
                showFolderExplanationPopup()
            } else {
                refreshMusics()
            }
        }
    }

    private fun launchFolderPicker() {
        folderPickerLauncher.launch(null)
    }

    private fun refreshMusics() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is MusicListFragment) {
            fragment.refreshMusics()
        }
    }

    private fun showHomeMusics() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MusicListFragment())
            .commit()
    }

    private fun setupFragments() {
        findViewById<View?>(R.id.menu_artists).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ArtistListFragment())
                .addToBackStack(null)
                .commit()
        }

        findViewById<View?>(R.id.menu_songs).setOnClickListener {
            showHomeMusics()
        }

        findViewById<View?>(R.id.menu_playlists).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlaylistListFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
