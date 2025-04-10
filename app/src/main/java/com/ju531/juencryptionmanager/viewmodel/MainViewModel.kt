package com.ju531.juencryptionmanager.viewmodel

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ju531.juencryptionmanager.ext.convertToString
import dagger.hilt.android.lifecycle.HiltViewModel
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    fun getSecretKey(): String {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256) // AES-256
        val secretKey = keyGenerator.generateKey()

        return secretKey.convertToString()
    }

}