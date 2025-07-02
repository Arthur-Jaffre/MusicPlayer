package fr.arthur.musicplayer.views.activities

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Album
import fr.arthur.musicplayer.viewModel.AlbumListViewModel
import org.koin.android.ext.android.inject

class EditAlbumActivity : BaseEditWithImageActivity<Album>() {

    private val albumViewModel: AlbumListViewModel by inject()
    private lateinit var currentAlbum: Album
    private lateinit var oldArtist: String
    private lateinit var albumEdit: EditText
    private lateinit var artistEdit: EditText
    private lateinit var yearEdit: EditText

    override fun getLayoutResId() = R.layout.activity_edit_album
    override fun getImageViewId() = R.id.edit_photo

    override fun initViews() {
        albumEdit = findViewById(R.id.edit_album)
        artistEdit = findViewById(R.id.edit_artist)
        yearEdit = findViewById(R.id.edit_year)
    }

    override fun loadFromIntent() {
        currentAlbum = (intent.getSerializableExtra("album") as? Album)!!
        oldArtist = currentAlbum.artistId
    }

    override fun populateForm() {
        albumEdit.setText(currentAlbum.name)
        artistEdit.setText(currentAlbum.artistId)
        loadImage(currentAlbum.imageUri)
    }

    override fun setupSaveButton() {
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            if (albumEdit.text.toString().isNotEmpty() && artistEdit.text.toString().isNotEmpty()) {
                val updatedAlbum = currentAlbum.copy(
                    name = albumEdit.text.toString(),
                    artistId = artistEdit.text.toString(),
                    imageUri = selectedImageUri?.toString() ?: currentAlbum.imageUri
                )
                albumViewModel.updateAlbum(updatedAlbum, oldArtist)
                Toast.makeText(this, R.string.updated_album_data, Toast.LENGTH_SHORT).show()
            }
            finish()

        }
    }
}
