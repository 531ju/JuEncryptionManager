package com.ju531.juencryptionmanager.model

sealed class EncryptionResult<out T> {
    data class Success<T>(val data : T) : EncryptionResult<T>()
    data class Error(val e: Throwable): EncryptionResult<Nothing>()
}

data class EncryptResultData (
    val data : String,
    val iv : ByteArray? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptResultData

        if (data != other.data) return false
        if (!iv.contentEquals(other.iv)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + iv.contentHashCode()
        return result
    }
}

data class DecryptResultData (
    val data : String
)