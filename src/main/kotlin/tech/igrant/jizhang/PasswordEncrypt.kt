package tech.igrant.jizhang

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

class PasswordEncrypt {
    companion object {
        private const val SHA_256 = "SHA-256"

        fun encrypt(password: String, salt: String): String {
            val digest = MessageDigest.getInstance(SHA_256)
            return digest
                    .digest("${password}${salt}".toByteArray(StandardCharsets.UTF_8))
                    .toString(StandardCharsets.UTF_8)
        }

        fun valid(inputPassword: String, encryptedPassword: String, salt: String): Boolean {
            return Objects.equals(encrypt(inputPassword, salt), encryptedPassword)
        }
    }
}
