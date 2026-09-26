package com.icerockdev.util

import java.security.MessageDigest
import org.springframework.security.crypto.bcrypt.BCrypt

private const val RANDOM_STRING_LENGTH = 10
private const val MAX_RANDOM_VALUE = 9

object HashUtils {
    fun sha512(input: String): String = hash("SHA-512", input)

    fun sha256(input: String): String = hash("SHA-256", input)

    fun sha1(input: String): String = hash("SHA-1", input)

    fun generatePasswordHash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verifyPassword(password: String, passwordHash: String): Boolean {
        return BCrypt.checkpw(password, passwordHash)
    }

    fun generateRandomToken(): String {
        return sha256(System.currentTimeMillis().toString() + generateRandomString(RANDOM_STRING_LENGTH))
    }

    fun generateRandomCode(length: Int): String {
        return (1..length)
            .map { (0..MAX_RANDOM_VALUE).random() }
            .joinToString("")
    }

    fun generateRandomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charPool.random() }
            .joinToString("")
    }


    private fun hash(algorithm: String, input: String): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
    }
}
