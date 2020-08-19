package tech.igrant.jizhang

import tech.igrant.jizhang.ext.toBase64
import tech.igrant.jizhang.ext.toMd5Hash
import tech.igrant.jizhang.ext.toSha256
import java.util.*

class PasswordEncrypt {
    companion object {

        fun encrypt(password: String, salt: String): String {
            return "${password}${salt}".toSha256().toMd5Hash().toBase64()
        }

        fun valid(inputPassword: String, encryptedPassword: String, salt: String): Boolean {
            return Objects.equals(encrypt(inputPassword, salt), encryptedPassword)
        }
    }

}