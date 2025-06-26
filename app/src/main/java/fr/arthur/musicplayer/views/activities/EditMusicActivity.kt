package fr.arthur.musicplayer.views.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import fr.arthur.musicplayer.R
import fr.arthur.musicplayer.models.Music
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import org.koin.android.ext.android.inject

class EditMusicActivity : AppCompatActivity() {

    private val viewModel: MusicListViewModel by inject()
    private lateinit var currentMusic: Music

    private lateinit var imageView: ImageView
    private lateinit var titleEdit: EditText
    private lateinit var albumEdit: EditText
    private lateinit var artistEdit: EditText
    private lateinit var yearEdit: EditText
    private lateinit var trackEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_music)

        initViews()
        loadMusicFromIntent()
        populateForm()
        setupSaveButton()
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
        albumEdit.setText(currentMusic.albumId)
        artistEdit.setText(currentMusic.artistId)
        yearEdit.setText(currentMusic.year?.toString().orEmpty())
        trackEdit.setText(currentMusic.trackNumber?.toString().orEmpty())

        currentMusic.imageUri?.let {
            imageView.setImageURI(it.toUri())
        }
    }

    private fun setupSaveButton() {
        findViewById<android.widget.Button>(R.id.btn_save).setOnClickListener {
            val updatedMusic = currentMusic.copy(
                title = titleEdit.text.toString().takeIf { it.isNotBlank() },
                albumId = albumEdit.text.toString(),
                artistId = artistEdit.text.toString(),
                year = yearEdit.text.toString().toIntOrNull(),
                trackNumber = trackEdit.text.toString().toIntOrNull()
            )
//            viewModel.updateMusic(updatedMusic)
            finish()
        }
    }
}