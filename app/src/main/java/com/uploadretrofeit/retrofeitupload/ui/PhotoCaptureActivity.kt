package com.uploadretrofeit.retrofeitupload.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationRequest
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.uploadretrofeit.retrofeitupload.R
import com.uploadretrofeit.retrofeitupload.databinding.ActivityPhotoCaptureBinding
import java.io.File

class PhotoCaptureActivity : AppCompatActivity() {
    lateinit var photoFile: File
    var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode==Activity.RESULT_OK){
            val capturePhoto = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding.ivPhoto.setImageBitmap(capturePhoto)
        }


    }

    lateinit var binding: ActivityPhotoCaptureBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient



    val locationRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED)

        {
            getLocationUpdates()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }
    private fun getPhotoFile(filename: String): File{
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(filename, ".jpg", storageDir)
    }


    override fun onResume() {
        super.onResume()
        binding.btnCapture.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile("photo_${System.currentTimeMillis()}")
            val fileProvider = FileProvider.getUriForFile(this,"com.uploadretrofeit.retrofeitupload.provider", photoFile)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            cameraLauncher.launch(cameraIntent)

        }
        getLocationUpdates()


    }

    private fun getLocationUpdates(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED){
            val locationRequest = com.google.android.gms.location.LocationRequest
                .Builder(10000)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .build()


            val locationCallBack = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    val lastLocation = p0.lastLocation
                    if (lastLocation!=null){
                        Toast.makeText(this@PhotoCaptureActivity,
                            "${lastLocation.latitude}, ${lastLocation.longitude}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallBack,
                Looper.getMainLooper()
            )
        } else{
            locationRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}