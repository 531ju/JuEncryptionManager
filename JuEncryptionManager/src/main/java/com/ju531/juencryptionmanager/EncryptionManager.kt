package com.ju531.juencryptionmanager

import android.os.Build
import androidx.annotation.RequiresApi
import com.ju531.juencryptionmanager.model.Decrypt
import com.ju531.juencryptionmanager.model.DecryptResultData
import com.ju531.juencryptionmanager.model.Encrypt
import com.ju531.juencryptionmanager.model.EncryptResultData
import com.ju531.juencryptionmanager.model.EncryptionResult
import com.ju531.juencryptionmanager.model.EncryptionType.Companion.convertToString
import javax.crypto.Cipher
import java.util.Base64
import javax.crypto.spec.IvParameterSpec

class EncryptionManager {

    fun encrypt(param : Encrypt) : EncryptionResult<EncryptResultData> {
        val cipher = Cipher.getInstance(param.type.convertToString())
        return runCatching {
            cipher.init(Cipher.ENCRYPT_MODE, param.key)

            val byte = param.text.toByteArray(Charsets.UTF_8)
            val encryptBytes = cipher.doFinal(byte)

            Base64.getEncoder().encodeToString(encryptBytes)
        }.fold(
            onSuccess = {
                val ivSpec = cipher.parameters
                if(ivSpec == null) {
                    EncryptionResult.Success(EncryptResultData(it))
                } else {
                    EncryptionResult.Success(
                        EncryptResultData(
                            data = it,
                            iv = ivSpec.getParameterSpec(IvParameterSpec::class.java).iv
                        )
                    )
                }
            },
            onFailure = {
                EncryptionResult.Error(it)
            }
        )
    }

    fun decrypt(param : Decrypt) : EncryptionResult<DecryptResultData> {
        val cipher = Cipher.getInstance(param.type.convertToString())
        return runCatching {
            if(param.iv == null) {
                cipher.init(Cipher.DECRYPT_MODE, param.key)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, param.key, IvParameterSpec(param.iv))
            }

            val base64EncryptedByte = param.text.toByteArray(Charsets.UTF_8)
            val encryptedByte = Base64.getDecoder().decode(base64EncryptedByte)
            cipher.doFinal(encryptedByte)
        }.fold(
            onSuccess = {
                EncryptionResult.Success(DecryptResultData(String(it)))
            },
            onFailure = {
                EncryptionResult.Error(it)
            }
        )
    }
}