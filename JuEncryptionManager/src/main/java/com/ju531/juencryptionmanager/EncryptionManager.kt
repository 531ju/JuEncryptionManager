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
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionManager {

    fun encrypt(param: Encrypt): EncryptionResult<EncryptResultData> = runCatching {
        val cipher = Cipher.getInstance(param.type.convertToString())
        val key = generateKey(param.key, param.type, true)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher.doFinal(param.text.toByteArray(Charsets.UTF_8))
        EncryptResultData(Base64.getEncoder().encodeToString(encrypted), cipher.iv)
    }.fold(
        onSuccess = { EncryptionResult.Success(it) },
        onFailure = { EncryptionResult.Error(it) }
    )

    fun decrypt(param: Decrypt): EncryptionResult<DecryptResultData> = runCatching {
        val cipher = Cipher.getInstance(param.type.convertToString())
        val key = generateKey(param.key, param.type, false)
        when {
            param.type == EncryptionType.AES_GCM_NO_PADDING -> {
                requireNotNull(param.iv) { "GCM requires the original IV" }
                cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, param.iv))
            }
            param.iv != null -> cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(param.iv))
            else -> cipher.init(Cipher.DECRYPT_MODE, key)
        }
        val encrypted = Base64.getDecoder().decode(param.text)
        DecryptResultData(String(cipher.doFinal(encrypted), Charsets.UTF_8))
    }.fold(
        onSuccess = { EncryptionResult.Success(it) },
        onFailure = { EncryptionResult.Error(it) }
    )

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