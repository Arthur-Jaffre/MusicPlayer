package fr.arthur.musicplayer.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.helpers.FolderUriStore
import fr.arthur.musicplayer.helpers.PermissionHandler
import fr.arthur.musicplayer.helpers.StorageDialog
import fr.arthur.musicplayer.manager.PlayerManager
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.views.fragments.lists.MusicListFragment
import fr.arthur.musicplayer.views.fragments.overviews.ArtistOverviewFragment
import fr.arthur.musicplayer.views.navigation.MainActivityFragmentNavigator


class MainActivity : AppCompatActivity() {

    private lateinit var folderUriStore: FolderUriStore
    private lateinit var miniPlayerController: MiniPlayerController
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var mainActivityFragmentNavigator: MainActivityFragmentNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        PlayerManager.init(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initMainActivity()
        initDimensions()
        mainActivityFragmentNavigator.setup()
        mainActivityFragmentNavigator.showHomeMusics()

        // Lancement strict de la chaîne de permissions depuis audio
        permissionHandler.requestAudioPermission()
    }

    private fun initDimensions() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent.getSerializableExtra("artist")?.let { artist ->
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ArtistOverviewFragment.newInstance(artist as Artist)
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initMainActivity() {
        findViewById<ImageButton>(R.id.btn_search).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        folderUriStore = FolderUriStore(this)
        permissionHandler = PermissionHandler(this, folderUriStore) { handleFolderAccess() }
        mainActivityFragmentNavigator = MainActivityFragmentNavigator(this)
        miniPlayerController = MiniPlayerController(this, findViewById(R.id.miniPlayerContainer))
    }

    private fun handleFolderAccess() {
        if (folderUriStore.get() == null) {
            StorageDialog(this) { permissionHandler.launchFolderPicker() }.show()
        } else {
            refreshMusics()
        }
    }

    private fun refreshMusics() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is MusicListFragment) {
            fragment.refreshMusics()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.handlePermissionResult(requestCode, grantResults)
    }

    override fun onDestroy() {
        miniPlayerController.destroy()
        super.onDestroy()
    }
}

