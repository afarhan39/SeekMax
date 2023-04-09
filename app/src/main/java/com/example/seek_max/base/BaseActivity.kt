package com.example.seek_max.base;

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seek_max.databinding.ActivityBaseBinding
import com.example.seek_max.util.customProgressDialog
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
open class BaseActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = customProgressDialog()
    }

    fun showProgress() {
        try {
            progressDialog?.show()
        } catch (e: Exception) { //do nothing
        }
    }

    fun hideProgress() {
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) { //do nothing
        }
    }

    override fun onDestroy() {
        try {
            if (progressDialog != null) {
                progressDialog?.dismiss()
                progressDialog = null
            }
        } catch (e: Exception) { //do nothing
        }
        super.onDestroy()
    }
}
