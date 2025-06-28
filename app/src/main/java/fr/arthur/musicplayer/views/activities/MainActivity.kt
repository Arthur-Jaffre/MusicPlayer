package fr.arthur.musicplayer.views.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.helpers.FolderUriStore
import fr.arthur.musicplayer.helpers.PermissionHandler
import fr.arthur.musicplayer.helpers.StorageDialog
import fr.arthur.musicplayer.helpers.appModule
import fr.arthur.musicplayer.views.fragments.lists.MusicListFragment
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

        folderUriStore = FolderUriStore(this)
        permissionHandler = PermissionHandler(this, folderUriStore) { refreshMusics() }
        mainActivityFragmentNavigator = MainActivityFragmentNavigator(this)

        if (!permissionHandler.hasAudioPermission()) {
            permissionHandler.requestAudioPermission()
        } else {
            handleFolderAccess()
        }

        mainActivityFragmentNavigator.showHomeMusics()
        mainActivityFragmentNavigator.setup()

        //DatabaseExporter.exportDatabaseWithWAL(this)
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