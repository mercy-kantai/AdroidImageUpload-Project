package com.uploadretrofeit.retrofeitupload.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uploadretrofeit.retrofeitupload.model.Photo
import com.uploadretrofeit.retrofeitupload.repository.PhotoRepository
import kotlinx.coroutines.launch


class PhotosViewModel: ViewModel() {
    val photoRepo = PhotoRepository()
    val errorLiveData = MutableLiveData<String>()
    val photoLiveData = MutableLiveData<Photo>()

    fun postPhoto(uri: Uri, caption:String){
        viewModelScope.launch {
          val response = photoRepo.uploadPhoto(uri, caption)
            if (response.isSuccessful){
                photoLiveData.postValue(response.body())
            }
            else {
                errorLiveData.postValue(response.errorBody()?.string())
            }

        }
    }
}
