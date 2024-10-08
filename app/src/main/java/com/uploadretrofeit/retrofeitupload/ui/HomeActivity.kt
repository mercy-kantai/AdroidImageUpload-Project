package com.uploadretrofeit.retrofeitupload.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.uploadretrofeit.retrofeitupload.R
import com.uploadretrofeit.retrofeitupload.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        binding.btnvHome.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.home->{
                    loadFragment(HomeFragment())
                }
                R.id.games->{
                    loadFragment(GamesFragment())
                }
                R.id.profile->{
                    loadFragment(ProfileFragment())
                }
                R.id.settings->{
                    loadFragment(SettingsFragment())
                }
                else -> {
                    loadFragment(HomeFragment())
                }

            }



        }
    }

    fun loadFragment(fragment: Fragment): Boolean{
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fcvHome, fragment)
        fragmentTransaction.commit()
        return true
    }

}