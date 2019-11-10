package tech.igrant.jizhang

import org.junit.Assert
import org.junit.jupiter.api.Test

class PasswordEncryptTest {

    @Test
    internal fun testEncryptAndValid() {
        val password = "123456"
        val salt = "W9a2A5q5KZq^?]AR"
        val encrypted = PasswordEncrypt.encrypt(password = password, salt = salt)
        Assert.assertTrue(PasswordEncrypt.valid(inputPassword = password, encryptedPassword = encrypted, salt = salt));
    }

}
