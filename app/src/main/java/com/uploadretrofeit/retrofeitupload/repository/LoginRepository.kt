package com.uploadretrofeit.retrofeitupload.repository

import com.uploadretrofeit.retrofeitupload.api.ApiClient
import com.uploadretrofeit.retrofeitupload.api.ApiInterface
import com.uploadretrofeit.retrofeitupload.model.LoginRequest
import com.uploadretrofeit.retrofeitupload.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Response

class LoginRepository {
    var apiClient = ApiClient.buildClient(ApiInterface::class.java)

    suspend fun login(loginRequest: LoginRequest):Response<LoginResponse>{
        return withContext(Dispatchers.IO){
            apiClient.login(loginRequest)
        }
    }


}