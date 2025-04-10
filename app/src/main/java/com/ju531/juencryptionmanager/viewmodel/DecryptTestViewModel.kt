package com.ju531.juencryptionmanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ju531.juencryptionmanager.EncryptionManager
import com.ju531.juencryptionmanager.model.Decrypt
import com.ju531.juencryptionmanager.model.EncryptionResult
import com.ju531.juencryptionmanager.model.EncryptionType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class DecryptTestViewModel @Inject constructor(
    private val encryptionManager : EncryptionManager
) : ViewModel() {

    private var key = ""
    private var encryptionType = EncryptionType.AES_CBC_NO_PADDING

    fun setKey(key: String) {
        this.key = key
    }

    fun setType(type : Int) {
        when(type) {
            0 -> encryptionType = EncryptionType.AES_CBC_NO_PADDING
            1 -> encryptionType = EncryptionType.AES_CBC_PKCS7_PADDING
            2 -> encryptionType = EncryptionType.AES_CTR_NO_PADDING
            3 -> encryptionType = EncryptionType.AES_ECB_NO_PADDING
            4 -> encryptionType = EncryptionType.AES_ECB_PKCS7_PADDING
            5 -> encryptionType = EncryptionType.AES_GCM_NO_PADDING
            6 -> encryptionType = EncryptionType.RSA_ECB_NO_PADDING
            7 -> encryptionType = EncryptionType.RSA_ECB_PKCS1_PADDING
            8 -> encryptionType = EncryptionType.RSA_ECB_OAEP_WITH_SHA1_AND_MGF1_PADDING
            9 -> encryptionType = EncryptionType.RSA_ECB_OAEP_WITH_SAH224_AND_MDF1_PADDING
            10 -> encryptionType = EncryptionType.RSA_ECB_OAEP_WITH_SAH256_AND_MDF1_PADDING
            11 -> encryptionType = EncryptionType.RSA_ECB_OAEP_WITH_SAH384_AND_MDF1_PADDING
            12 -> encryptionType = EncryptionType.RSA_ECB_OAEP_WITH_SAH512_AND_MDF1_PADDING
            13 -> encryptionType = EncryptionType.RSA_ECB_OAEP_PADDING
        }
    }

    fun decrypt(text: String, iv: String?): String {
        return encryptionManager.decrypt(
            Decrypt(
                type = encryptionType,
                text = text,
                key = key,
                iv = iv?.toByteArray()
            )
        ).let { result ->
            when (result) {
                is EncryptionResult.Success -> {
                    result.data.data
                }

                is EncryptionResult.Error -> {
                    Log.d("decrypt_test", "error : ${result.e}")
                    "decrypt failed "
                }
                else -> {
                    "decrypt failed "
                }
            }
        }
    }
}