package com.ju531.juencryptionmanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ju531.juencryptionmanager.EncryptionManager
import com.ju531.juencryptionmanager.ext.convertToString
import com.ju531.juencryptionmanager.model.Encrypt
import com.ju531.juencryptionmanager.model.EncryptionResult
import com.ju531.juencryptionmanager.model.EncryptionType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class EncryptTestViewModel @Inject constructor(
    private val encryptionManager: EncryptionManager
) : ViewModel() {

    private var key = ""
    private var encryptionType = EncryptionType.AES_CBC_NO_PADDING

    fun setKey(key : String) {
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

    fun encrypt(text : String) : Pair<String, String?> {
        return encryptionManager.encrypt(
            Encrypt(
                type = encryptionType,
                text = text,
                key = key
            )
        ).let { result ->
            when(result) {
                is EncryptionResult.Success -> {
                    val encryptedText = result.data.data
                    val iv = result.data.iv?.let {
                        Base64.getEncoder().encodeToString(it)
                    }
                    Pair(result.data.data, iv)
                }
                is EncryptionResult.Error -> {
                    Log.d("encrypt_test", "error : ${result.e}")
                    Pair("encrypt failed ", null)
                }
                else -> {
                    Pair("encrypt failed ", null)
                }
            }
        }
    }

}