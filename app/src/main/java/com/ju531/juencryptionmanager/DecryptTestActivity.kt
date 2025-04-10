package com.ju531.juencryptionmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ju531.juencryptionmanager.databinding.ActivityDecryptTestBinding
import com.ju531.juencryptionmanager.viewmodel.DecryptTestViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class DecryptTestActivity : AppCompatActivity() {

    private var _binding : ActivityDecryptTestBinding? = null
    val binding get() = _binding ?: throw NullPointerException("binding is null")

    private val viewModel : DecryptTestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDecryptTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null) {
            setupArguments()
        }
        bindData()
        setupView()
    }

    private fun setupArguments() {
        val key = intent.getStringExtra("key") ?: ""
        viewModel.setKey(key)
    }

    private fun setupView() {
        with(binding) {
            npEncryptionType.setOnValueChangedListener { _, _, newValue ->
                viewModel.setType(newValue)
            }

            btnDecrypt.setOnClickListener {
                if(etText.text.isNotEmpty()) {
                    val iv = etIv.text.toString().takeIf { it.isNotEmpty() }
                    val decryptResult = viewModel.decrypt(etText.text.toString(), iv)
                    tvResult.text = decryptResult
                }
            }
        }
    }

    private fun bindData() {
        with(binding) {
            npEncryptionType.apply {
                val displayValues = arrayListOf<String>(
                    "AES_CBC_NO_PADDING",
                    "AES_CBC_PKCS7_PADDING",
                    "AES_CTR_NO_PADDING",
                    "AES_ECB_NO_PADDING",
                    "AES_ECB_PKCS7_PADDING",
                    "AES_GCM_NO_PADDING",
                    "RSA_ECB_NO_PADDING",
                    "RSA_ECB_PKCS1_PADDING",
                    "RSA_ECB_OAEP_WITH_SHA1_AND_MGF1_PADDING",
                    "RSA_ECB_OAEP_WITH_SAH224_AND_MDF1_PADDING",
                    "RSA_ECB_OAEP_WITH_SAH256_AND_MDF1_PADDING",
                    "RSA_ECB_OAEP_WITH_SAH384_AND_MDF1_PADDING",
                    "RSA_ECB_OAEP_WITH_SAH512_AND_MDF1_PADDING",
                    "RSA_ECB_OAEP_PADDING"
                )

                displayedValues = displayValues.toTypedArray()
                minValue = 0
                maxValue = displayValues.size - 1
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}