package fr.arthur.musicplayer.views.activities.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

abstract class BaseEditWithImageActivity<T> : AppCompatActivity() {

    protected lateinit var imageView: ImageView
    protected var selectedImageUri: Uri? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        initImagePicker()
        initImageView()
        initViews()
        loadFromIntent()
        populateForm()
        setupSaveButton()
    }

    private fun initImageView() {
        imageView = findViewById(getImageViewId())

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

    private fun initImagePicker() {
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
                if (isGranted) openImagePicker()
            }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        imagePickerLauncher.launch(intent)
    }

    protected fun loadImage(uriString: String?) {
        uriString?.let {
            imageView.setImageURI(it.toUri())
        }
    }

    // Méthodes que chaque activité enfant doit définir explicitement :
    protected abstract fun getLayoutResId(): Int
    protected abstract fun getImageViewId(): Int
    protected abstract fun initViews()
    protected abstract fun loadFromIntent()
    protected abstract fun populateForm()
    protected abstract fun setupSaveButton()
}
