package com.ju531.juencryptionmanager

import com.ju531.juencryptionmanager.model.Decrypt
import com.ju531.juencryptionmanager.model.DecryptResultData
import com.ju531.juencryptionmanager.model.Encrypt
import com.ju531.juencryptionmanager.model.EncryptResultData
import com.ju531.juencryptionmanager.model.EncryptionResult
import com.ju531.juencryptionmanager.model.EncryptionType
import com.ju531.juencryptionmanager.model.EncryptionType.Companion.convertToString
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionManager {

    fun encrypt(param : Encrypt) : EncryptionResult<EncryptResultData> {
        val cipher = Cipher.getInstance(param.type.convertToString())
        return runCatching {
            val key = generateKey(param.key, param.type, true)
            cipher.init(Cipher.ENCRYPT_MODE, key)

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
            val key = generateKey(param.key, param.type, false)
            if(param.iv == null) {
                cipher.init(Cipher.DECRYPT_MODE, key)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(param.iv))
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

    private fun generateKey(key : String, type : EncryptionType, isEncrypt : Boolean) : Key {
        return when(type) {
            EncryptionType.AES_CBC_NO_PADDING,
            EncryptionType.AES_CBC_PKCS7_PADDING,
            EncryptionType.AES_CTR_NO_PADDING,
            EncryptionType.AES_ECB_NO_PADDING,
            EncryptionType.AES_ECB_PKCS7_PADDING,
            EncryptionType.AES_GCM_NO_PADDING -> {
                generateSecretKey(key)
            }
            EncryptionType.RSA_ECB_NO_PADDING,
            EncryptionType.RSA_ECB_PKCS1_PADDING,
            EncryptionType.RSA_ECB_OAEP_WITH_SHA1_AND_MGF1_PADDING,
            EncryptionType.RSA_ECB_OAEP_WITH_SAH224_AND_MDF1_PADDING,
            EncryptionType.RSA_ECB_OAEP_WITH_SAH256_AND_MDF1_PADDING,
            EncryptionType.RSA_ECB_OAEP_WITH_SAH384_AND_MDF1_PADDING,
            EncryptionType.RSA_ECB_OAEP_WITH_SAH512_AND_MDF1_PADDING,
            EncryptionType.RSA_ECB_OAEP_PADDING -> {
                if(isEncrypt) {
                    generatePublicKey(key)
                } else {
                    generatePrivateKey(key)
                }
            }
        }
    }

    private fun generateSecretKey(key : String) : SecretKey {
        val decodedKey = Base64.getDecoder().decode(key)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
    }

    private fun generatePrivateKey(key : String) : PrivateKey {
        val keyBytes = Base64.getDecoder().decode(key)
        val spec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(spec)
    }

    private fun generatePublicKey(key : String) : PublicKey {
        val keyBytes = Base64.getDecoder().decode(key)
        val spec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(spec)
    }
}