package com.uploadretrofeit.retrofeitupload.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uploadretrofeit.retrofeitupload.R
import com.uploadretrofeit.retrofeitupload.databinding.ActivityLoginBinding
import com.uploadretrofeit.retrofeitupload.model.LoginRequest
import com.uploadretrofeit.retrofeitupload.model.LoginResponse
import com.uploadretrofeit.retrofeitupload.utils.Constants
import com.uploadretrofeit.retrofeitupload.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityLoginBinding.inflate(layoutInflater)
        redirectUser()
        setContentView(binding.root)
    }

    fun redirectUser(){
        val sharedPreferences = getSharedPreferences("RETROFIETUPLOAD_PREFS", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(Constants.ACCESS_TOKEN, "")
        if (token!!.isNotBlank()){
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.button.setOnClickListener {
            validateLogin()
        }
        loginViewModel.errorLiveData.observe(this){error->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
        loginViewModel.loginLiveData.observe(this){loginResponse->
            Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
            persistLogin(loginResponse)
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
    private fun persistLogin(loginResponse: LoginResponse){
        val sharedPreferences = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.FIRST_NAME, loginResponse.firstName)
        editor.putString(Constants.LAST_NAME, loginResponse.lastName)
        editor.putString(Constants.ACCESS_TOKEN, loginResponse.accessToken)
        editor.apply()
    }

    private fun validateLogin(){
        val username = binding.etusername.text.toString()
        val password = binding.etpassword.text.toString()
        var error = false

        if (username.isBlank()){
            binding.tilUsername.error = " Username is required"
            error=true
        }
        if (password.isBlank()){
            binding.tilPassword.error = "Password is required"
            error=true
        }
        if (!error){
            val loginRequest = LoginRequest( username, password)
            loginViewModel.login(loginRequest)
        }
    }
}