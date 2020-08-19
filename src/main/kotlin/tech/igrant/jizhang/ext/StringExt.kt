package tech.igrant.jizhang.ext

import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

val objectMapper = ObjectMapper()

fun Any.toJSON(): String {
    return objectMapper.writeValueAsString(this)
}

fun String.toBase64(): String {
    return Base64.getEncoder().encode(this.toByteArray()).toString(StandardCharsets.UTF_8)
}

private const val MD5 = "MD5"

fun String.toMd5Hash(): String {
    val md5Hash = MessageDigest.getInstance(MD5)
    return md5Hash.digest(this.toByteArray(StandardCharsets.UTF_8)).toString(StandardCharsets.UTF_8)
}

private const val SHA_256 = "SHA-256"

fun String.toSha256(): String {
    val hash = MessageDigest.getInstance(SHA_256)
    return hash.digest(this.toByteArray(StandardCharsets.UTF_8)).toString(StandardCharsets.UTF_8)
}

fun randomString(len: Int = 16): String {
    val sources = ('0'..'z').toList().toTypedArray()
    return (1..len).map { sources.random() }.joinToString("")
}