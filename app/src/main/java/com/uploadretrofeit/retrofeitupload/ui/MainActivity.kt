package com.uploadretrofeit.retrofeitupload.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import com.uploadretrofeit.retrofeitupload.R
import com.uploadretrofeit.retrofeitupload.databinding.ActivityMainBinding
import com.uploadretrofeit.retrofeitupload.utils.Constants
import com.uploadretrofeit.retrofeitupload.viewmodel.PhotosViewModel

class MainActivity : AppCompatActivity() {
    val photosViewModel: PhotosViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                photoUri = uri
            }
        }
        binding.btnUpload.setOnClickListener {
            validateForm()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.ivPhoto.setOnClickListener {
            val mimeType = "image/*"
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.SingleMimeType(
                        mimeType)))
        }
        photosViewModel.errorLiveData.observe(this){error->
            Toast.makeText(this, error, Toast.LENGTH_LONG)
        }
        photosViewModel.photoLiveData.observe(this){photoResponse->
            val imageUrl = "${Constants.BASEURL}${photoResponse.image}"
            Picasso.get()
                   .load(imageUrl)
                   .into(binding.ivPhoto)
        }
    }

    private fun validateForm() {
        var error = false
        if (photoUri == null) {
            error = true
            Toast.makeText(this, "Please select photo", Toast.LENGTH_LONG).show()
        }

        var caption = binding.etCaption.text.toString()
        if (caption.isBlank()){
            error = true
            binding.etCaption.error = "Caption required"
        }
        if (!error){
            photosViewModel.postPhoto(photoUri!!,caption)
        }
    }
}
