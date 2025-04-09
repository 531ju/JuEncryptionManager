package com.ju531.juencryptionmanager.model

enum class EncryptionType {
    AES_CBC_NO_PADDING,
    AES_CBC_PKCS7_PADDING,
    AES_CTR_NO_PADDING,
    AES_ECB_NO_PADDING,
    AES_ECB_PKCS7_PADDING,
    AES_GCM_NO_PADDING,
    RSA_ECB_NO_PADDING,
    RSA_ECB_PKCS1_PADDING,
    RSA_ECB_OAEP_WITH_SHA1_AND_MGF1_PADDING,
    RSA_ECB_OAEP_WITH_SAH224_AND_MDF1_PADDING,
    RSA_ECB_OAEP_WITH_SAH256_AND_MDF1_PADDING,
    RSA_ECB_OAEP_WITH_SAH384_AND_MDF1_PADDING,
    RSA_ECB_OAEP_WITH_SAH512_AND_MDF1_PADDING,
    RSA_ECB_OAEP_PADDING;

    companion object {
        fun EncryptionType.convertToString() : String = when(this) {
            AES_CBC_NO_PADDING                        -> "AES/CBC/NoPadding"
            AES_CBC_PKCS7_PADDING                     -> "AES/CBC/PKCS7Padding"
            AES_CTR_NO_PADDING                        -> "AES/CTR/NoPadding"
            AES_ECB_NO_PADDING                        -> "AES/ECB/NoPadding"
            AES_ECB_PKCS7_PADDING                     -> "AES/ECB/PKCS7Padding"
            AES_GCM_NO_PADDING                        -> "AES/GCM/NoPadding"
            RSA_ECB_NO_PADDING                        -> "RSA/ECB/NoPadding"
            RSA_ECB_PKCS1_PADDING                     -> "RSA/ECB/PKCS1Padding"
            RSA_ECB_OAEP_WITH_SHA1_AND_MGF1_PADDING   -> "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"
            RSA_ECB_OAEP_WITH_SAH224_AND_MDF1_PADDING -> "RSA/ECB/OAEPWithSHA-224AndMGF1Padding"
            RSA_ECB_OAEP_WITH_SAH256_AND_MDF1_PADDING -> "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
            RSA_ECB_OAEP_WITH_SAH384_AND_MDF1_PADDING -> "RSA/ECB/OAEPWithSHA-384AndMGF1Padding"
            RSA_ECB_OAEP_WITH_SAH512_AND_MDF1_PADDING -> "RSA/ECB/OAEPWithSHA-512AndMGF1Padding"
            RSA_ECB_OAEP_PADDING                      -> "RSA/ECB/OAEPPadding"
        }
    }
}