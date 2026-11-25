package com.ju531.juencryptionmanager

import com.ju531.juencryptionmanager.model.*
import org.junit.Assert.*
import org.junit.Test
import java.security.KeyPairGenerator
import java.util.Base64
import javax.crypto.KeyGenerator

class EncryptionManagerTest {
    private val manager = EncryptionManager()
    private fun encoded(bytes: ByteArray) = Base64.getEncoder().encodeToString(bytes)
    private fun aesKey() = encoded(KeyGenerator.getInstance("AES").apply { init(128) }.generateKey().encoded)

    @Test fun gcmRoundTripAndTampering() {
        val key = aesKey()
        val type = EncryptionType.AES_GCM_NO_PADDING
        val encrypted = manager.encrypt(Encrypt(type, "한글 😀", key)) as EncryptionResult.Success
        assertNotNull(encrypted.data.iv)
        val decrypted = manager.decrypt(Decrypt(type, encrypted.data.data, key, encrypted.data.iv)) as EncryptionResult.Success
        assertEquals("한글 😀", decrypted.data.data)
        val bytes = Base64.getDecoder().decode(encrypted.data.data)
        bytes[0] = (bytes[0].toInt() xor 1).toByte()
        assertTrue(manager.decrypt(Decrypt(type, encoded(bytes), key, encrypted.data.iv)) is EncryptionResult.Error)
        assertTrue(manager.decrypt(Decrypt(type, encrypted.data.data, key)) is EncryptionResult.Error)
    }

    @Test fun oaepDoesNotRequireIv() {
        val keys = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
        val type = EncryptionType.RSA_ECB_OAEP_WITH_SHA1_AND_MGF1_PADDING
        val encrypted = manager.encrypt(Encrypt(type, "round trip", encoded(keys.public.encoded))) as EncryptionResult.Success
        assertNull(encrypted.data.iv)
        val decrypted = manager.decrypt(Decrypt(type, encrypted.data.data, encoded(keys.private.encoded))) as EncryptionResult.Success
        assertEquals("round trip", decrypted.data.data)
    }

    @Test fun ctrRoundTripAndInvalidKey() {
        val key = aesKey()
        val type = EncryptionType.AES_CTR_NO_PADDING
        val encrypted = manager.encrypt(Encrypt(type, "UTF-8 한글", key)) as EncryptionResult.Success
        val decrypted = manager.decrypt(Decrypt(type, encrypted.data.data, key, encrypted.data.iv)) as EncryptionResult.Success
        assertEquals("UTF-8 한글", decrypted.data.data)
        assertTrue(manager.encrypt(Encrypt(type, "", "!")) is EncryptionResult.Error)
        assertTrue(manager.decrypt(Decrypt(type, "!", key, encrypted.data.iv)) is EncryptionResult.Error)
    }
}
