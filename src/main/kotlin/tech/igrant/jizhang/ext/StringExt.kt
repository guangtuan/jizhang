package tech.igrant.jizhang.ext

import com.fasterxml.jackson.databind.ObjectMapper

val objectMapper = ObjectMapper()

fun Any.toJSON(): String {
    return objectMapper.writeValueAsString(this)
}