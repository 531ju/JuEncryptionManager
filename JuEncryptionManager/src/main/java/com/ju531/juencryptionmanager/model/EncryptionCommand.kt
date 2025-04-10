package com.ju531.juencryptionmanager.model

sealed class EncryptionCommand {
    abstract val type : EncryptionType
    abstract val text : String
    abstract val key : String
}

data class Encrypt(
    override val type : EncryptionType,
    override val text : String,
    override val key : String
) : EncryptionCommand()

data class Decrypt(
    override val type: EncryptionType,
    override val text: String,
    override val key: String,
    val iv: ByteArray? = null
) : EncryptionCommand() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Decrypt

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
