package com.ju531.juencryptionmanager.model

import java.security.Key

data class EncryptParam (
    val type : EncryptionType,
    val text : String,
    val key : Key
)
data class DecryptParam (
    val type : EncryptionType,
    val text : String,
    val key : Key,
    val iv: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DecryptParam

        if (type != other.type) return false
        if (text != other.text) return false
        if (key != other.key) return false
        if (iv != null) {
            if (other.iv == null) return false
            if (!iv.contentEquals(other.iv)) return false
        } else if (other.iv != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + (iv?.contentHashCode() ?: 0)
        return result
    }
}