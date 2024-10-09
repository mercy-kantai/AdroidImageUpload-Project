package com.uploadretrofeit.retrofeitupload.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uploadretrofeit.retrofeitupload.model.LoginRequest
import com.uploadretrofeit.retrofeitupload.model.LoginResponse
import com.uploadretrofeit.retrofeitupload.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    val loginLiveData = MutableLiveData<LoginResponse>()
    val errorLiveData = MutableLiveData<String>()
    val loginRepo = LoginRepository()

    fun login(loginRequest: LoginRequest){
        viewModelScope.launch {
            val response = loginRepo.login(loginRequest)
            if (response.isSuccessful){
                loginLiveData.postValue(response.body())
            }else{
                errorLiveData.postValue(response.errorBody()?.string())
            }
        }
    }
}