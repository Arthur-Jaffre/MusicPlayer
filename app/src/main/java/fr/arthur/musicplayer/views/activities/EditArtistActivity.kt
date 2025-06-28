package fr.arthur.musicplayer.views.activities

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Artist
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import org.koin.android.ext.android.inject

class EditArtistActivity : BaseEditWithImageActivity<Artist>() {

    private val musicViewModel: ArtistListViewModel by inject()
    private lateinit var currentArtist: Artist
    private lateinit var lastNameArtist: String
    private lateinit var artistEdit: EditText

    override fun getLayoutResId() = R.layout.activity_edit_artist
    override fun getImageViewId() = R.id.edit_photo

    override fun initViews() {
        artistEdit = findViewById(R.id.edit_artist)
    }

    override fun loadFromIntent() {
        currentArtist = (intent.getSerializableExtra("artist") as? Artist)!!
        lastNameArtist = currentArtist.id
    }

    override fun populateForm() {
        artistEdit.setText(currentArtist.id)
        loadImage(currentArtist.imageUri)
    }

    override fun setupSaveButton() {
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            if (artistEdit.text.toString().isNotEmpty()) {
                val updatedArtist = currentArtist.copy(
                    id = artistEdit.text.toString(),
                    imageUri = selectedImageUri?.toString() ?: currentArtist.imageUri
                )
                musicViewModel.updateArtist(updatedArtist, lastNameArtist)
                Toast.makeText(this, R.string.updated_artist_data, Toast.LENGTH_SHORT).show()
            }
            finish()

        }
    }
}
