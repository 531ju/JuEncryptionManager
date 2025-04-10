package com.ju531.juencryptionmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ju531.juencryptionmanager.databinding.ActivityMainBinding
import com.ju531.juencryptionmanager.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    val binding get() = _binding ?: throw NullPointerException("binding is null")

    private val vieWModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        with(binding) {
            btnGenerateKey.setOnClickListener {
                val key = vieWModel.getSecretKey()
                etKey.setText(key)
            }

            btnEncryptTest.setOnClickListener {
                if(etKey.text.isNotEmpty()) {
                    val intent = Intent(this@MainActivity, EncryptTestActivity::class.java)
                    intent.putExtra("key", etKey.text.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "please fill your key", Toast.LENGTH_SHORT).show()
                }
            }

            btnDecryptTest.setOnClickListener {
                if(etKey.text.isNotEmpty()) {
                    val intent = Intent(this@MainActivity, DecryptTestActivity::class.java)
                    intent.putExtra("key", etKey.text.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "please fill your key", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}