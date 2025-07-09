package fr.arthur.musicplayer.views.activities

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.viewModel.AlbumListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.views.activities.utils.BaseEditWithImageActivity
import org.koin.android.ext.android.inject

class EditMusicActivity : BaseEditWithImageActivity<Music>() {

    private val musicViewModel: MusicListViewModel by inject()
    private val albumViewModel: AlbumListViewModel by inject()

    private lateinit var currentMusic: Music

    private lateinit var titleEdit: EditText
    private lateinit var albumEdit: EditText
    private lateinit var artistEdit: EditText
    private lateinit var yearEdit: EditText
    private lateinit var trackEdit: EditText

    override fun getLayoutResId() = R.layout.activity_edit_music
    override fun getImageViewId() = R.id.edit_photo

    override fun initViews() {
        titleEdit = findViewById(R.id.edit_title)
        albumEdit = findViewById(R.id.edit_album)
        artistEdit = findViewById(R.id.edit_artist)
        yearEdit = findViewById(R.id.edit_year)
        trackEdit = findViewById(R.id.edit_track)
    }

    override fun loadFromIntent() {
        val music = intent.getSerializableExtra("music") as? Music
        if (music == null) {
            finish()
            return
        }
        currentMusic = music
    }

    override fun populateForm() {
        titleEdit.setText(currentMusic.title.orEmpty())
        artistEdit.setText(currentMusic.artistIdsAsString())
        yearEdit.setText(currentMusic.year?.toString().orEmpty())
        trackEdit.setText(currentMusic.trackNumber?.toString().orEmpty())
        loadImage(currentMusic.imageUri)

        albumViewModel.getAlbumById(currentMusic.albumId)
        albumViewModel.albumObservable.observe { album ->
            albumEdit.setText(album.name)
        }
    }

    override fun setupSaveButton() {
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val artists = artistEdit.text.split(",").map { it.trim() }.filter { it.isNotBlank() }
            if (titleEdit.text.isNotEmpty() && artists.isNotEmpty()) {
                val updatedMusic = currentMusic.copy(
                    title = titleEdit.text.toString().trim().takeIf { it.isNotBlank() },
                    albumId = albumEdit.text.toString().trim(),
                    artistIds = artists,
                    year = yearEdit.text.toString().toIntOrNull(),
                    trackNumber = trackEdit.text.toString().toIntOrNull(),
                    imageUri = selectedImageUri?.toString() ?: currentMusic.imageUri
                )

                musicViewModel.updateMusic(updatedMusic)
                Toast.makeText(this, R.string.updated_data, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, R.string.music_missing_data, Toast.LENGTH_SHORT).show()
            }


        }
    }
}

