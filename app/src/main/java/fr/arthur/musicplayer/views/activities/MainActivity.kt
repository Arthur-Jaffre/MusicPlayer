package fr.arthur.musicplayer.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.helpers.FolderUriStore
import fr.arthur.musicplayer.helpers.PermissionHandler
import fr.arthur.musicplayer.helpers.StorageDialog
import fr.arthur.musicplayer.helpers.appModule
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.views.fragments.lists.MusicListFragment
import fr.arthur.musicplayer.views.fragments.overviews.ArtistOverviewFragment
import fr.arthur.musicplayer.views.navigation.MainActivityFragmentNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MainActivity : AppCompatActivity() {

    private lateinit var folderUriStore: FolderUriStore
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var mainActivityFragmentNavigator: MainActivityFragmentNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        GlobalContext.startKoin { androidContext(this@MainActivity); modules(appModule) }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initMainActivity()

        folderUriStore = FolderUriStore(this)
        permissionHandler = PermissionHandler(this, folderUriStore) { refreshMusics() }
        mainActivityFragmentNavigator = MainActivityFragmentNavigator(this)

        if (!permissionHandler.hasAudioPermission()) {
            permissionHandler.requestAudioPermission()
        } else {
            handleFolderAccess()
        }

        mainActivityFragmentNavigator.setup()
        mainActivityFragmentNavigator.showHomeMusics()

        //DatabaseExporter.exportDatabaseWithWAL(this)
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

}