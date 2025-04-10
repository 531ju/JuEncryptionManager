package com.ju531.juencryptionmanager.ext

import java.math.BigInteger
import java.security.Key
import java.util.Base64

fun String.convertToByteArray(): ByteArray {
    var returnData: List<Byte> = ArrayList()

    val str = toString().replace(" ", "")
    var hex1 = str.replace("0X", "")
    var hex = hex1.replace("0x", "")

    hex = hex.uppercase()

    var intCount = (hex.length%2) == 0
    if(intCount) {
        try {
            returnData = ArrayList()
            for(i in 0..hex.length step  2) {
                if(i<hex.length) {
                    var subString = hex.substring(i until i+2)
                    returnData.add(BigInteger(subString, 16).toByte())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return returnData.toByteArray()
}

fun ByteArray.convertToString() : String {
    return StringBuilder().let {
        for (byte in this) {
            it.append(String.format("0x%02x", byte)).append(" ")
        }
        it
    }.toString()
}

fun Key.convertToString() : String {
    val encodedKey = this.encoded
    return Base64.getEncoder().encodeToString(encodedKey)
}