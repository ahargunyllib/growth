package com.ahargunyllib.growth.utils

import android.util.Base64

object EncryptionUtil {
    private const val MIN_KEY_LENGTH = 8

    private fun stringToBytes(str: String): ByteArray {
        return str.toByteArray(Charsets.UTF_8)
    }

    private fun bytesToString(bytes: ByteArray): String {
        return String(bytes, Charsets.UTF_8)
    }

    private fun xorDecrypt(encrypted: String, key: String): String {
        try {
            val encryptedBytes = Base64.decode(encrypted, Base64.NO_WRAP)
            val keyBytes = stringToBytes(key)

            val decrypted = ByteArray(encryptedBytes.size)
            for (i in encryptedBytes.indices) {
                decrypted[i] = (encryptedBytes[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
            }

            return bytesToString(decrypted)
        } catch (e: Exception) {
            throw IllegalArgumentException("Decryption failed: ${e.message}")
        }
    }

    fun decryptData(encrypted: String, key: String): String {
        require(key.length >= MIN_KEY_LENGTH) { "Key must be at least $MIN_KEY_LENGTH characters" }
        return xorDecrypt(encrypted, key)
    }
}
