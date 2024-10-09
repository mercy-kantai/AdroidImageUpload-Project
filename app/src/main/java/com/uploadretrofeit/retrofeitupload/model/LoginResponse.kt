package com.uploadretrofeit.retrofeitupload.model

data class LoginResponse(
    var id: Int,
    var username: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var gender: String,
    var image: String,
    var accessToken: String,
    var refreshToken: String,
)
