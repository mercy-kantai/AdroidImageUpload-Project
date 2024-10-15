package com.uploadretrofeit.retrofeitupload.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.uploadretrofeit.retrofeitupload.R
import com.uploadretrofeit.retrofeitupload.databinding.FragmentSettingsBinding
import com.uploadretrofeit.retrofeitupload.utils.Constants


class SettingsFragment : Fragment() {
    var fsbbinding: FragmentSettingsBinding? = null
    val binding get() = fsbbinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fsbbinding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onResume() {
        super.onResume()
        binding.btnLogout.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes"){dialog, which->
                    logoutUser()
                }
                .setNegativeButton("No"){dialog, which->
                    dialog.dismiss()
                }
             dialog.show()
        }
    }

    private fun logoutUser(){
        val prefs = requireContext().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(Constants.ACCESS_TOKEN)
        editor.remove(Constants.FIRST_NAME)
        editor.remove(Constants.LAST_NAME)
        editor.apply()

        startActivity(Intent(requireActivity(),LoginActivity::class.java))


    }

    override fun onDestroyView() {
        super.onDestroyView()
        fsbbinding = null
    }

}