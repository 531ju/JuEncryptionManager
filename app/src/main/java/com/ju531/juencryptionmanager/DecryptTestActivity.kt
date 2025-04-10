package com.ju531.juencryptionmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ju531.juencryptionmanager.databinding.ActivityDecryptTestBinding

class DecryptTestActivity : AppCompatActivity() {

    private var _binding : ActivityDecryptTestBinding? = null
    val binding get() = _binding ?: throw NullPointerException("binding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDecryptTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}