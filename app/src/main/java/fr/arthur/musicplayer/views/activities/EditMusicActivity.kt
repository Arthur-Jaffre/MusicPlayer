package fr.arthur.musicplayer.views.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.net.toUri
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.viewModel.AlbumListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import org.koin.android.ext.android.inject

class EditMusicActivity : AppCompatActivity() {

    private val musicViewModel: MusicListViewModel by inject()
    private val albumViewModel: AlbumListViewModel by inject()

    private lateinit var currentMusic: Music

    private lateinit var imageView: ImageView
    private lateinit var titleEdit: EditText
    private lateinit var albumEdit: EditText
    private lateinit var artistEdit: EditText
    private lateinit var yearEdit: EditText
    private lateinit var trackEdit: EditText

    private var selectedImageUri: Uri? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_music)

        initViews()
        initActivityResultLaunchers()
        loadMusicFromIntent()
        initAlbum()
        imageManagement()
        populateForm()
        setupSaveButton()
    }

    private fun initAlbum() {
        albumViewModel.getAlbumById(currentMusic.albumId)
        albumViewModel.albumObservable.observe { album ->
            albumEdit.setText(album.name)
        }
    }

    private fun imageManagement() {
        imageView.setOnClickListener {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

            if (ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                permissionLauncher.launch(permission)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        imagePickerLauncher.launch(intent)
    }

    private fun initActivityResultLaunchers() {
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val uri = result.data?.data
                    if (uri != null) {
                        selectedImageUri = uri
                        imageView.setImageURI(uri)
                    }
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    openImagePicker()
                }
            }
    }

    private fun initViews() {
        imageView = findViewById(R.id.edit_photo)
        titleEdit = findViewById(R.id.edit_title)
        albumEdit = findViewById(R.id.edit_album)
        artistEdit = findViewById(R.id.edit_artist)
        yearEdit = findViewById(R.id.edit_year)
        trackEdit = findViewById(R.id.edit_track)
    }

    private fun loadMusicFromIntent() {
        val music = intent.getSerializableExtra("music") as? Music
        if (music == null) {
            finish()
            return
        }
        currentMusic = music
    }

    private fun populateForm() {
        titleEdit.setText(currentMusic.title.orEmpty())
        artistEdit.setText(currentMusic.artistIdsAsString())
        yearEdit.setText(currentMusic.year?.toString().orEmpty())
        trackEdit.setText(currentMusic.trackNumber?.toString().orEmpty())

        currentMusic.imageUri?.let {
            imageView.setImageURI(it.toUri())
        }
    }

    private fun setupSaveButton() {
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val updatedMusic = currentMusic.copy(
                title = titleEdit.text.toString().takeIf { it.isNotBlank() },
                albumId = albumEdit.text.toString(),
                artistIds = artistEdit.text.split(",").map { it.trim() }.filter { it.isNotBlank() },
                year = yearEdit.text.toString().toIntOrNull(),
                trackNumber = trackEdit.text.toString().toIntOrNull(),
                imageUri = selectedImageUri?.toString() ?: currentMusic.imageUri
            )

            // On suppose que la permission SAF persistante est déjà acquise,
            // on écrit directement dans le fichier via VM
            musicViewModel.updateMusic(updatedMusic)

            Toast.makeText(this, R.string.updated_data, Toast.LENGTH_SHORT).show()

            finish()
        }
    }
}
