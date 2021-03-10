package tech.igrant.jizhang.session

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

interface SessionRepo {

    fun invalid(email: String, token: String): Boolean
    fun save(email: String, token: String)
    fun getToken(email: String): String?

    @Component
    class Impl(private val stringRedisTemplate: StringRedisTemplate) : SessionRepo {

        private fun String.toMainKey(): String {
            return "token:$this"
        }

        private fun String.exchangeToken(): String? {
            return stringRedisTemplate.opsForValue().get(this.toMainKey())
        }

        override fun getToken(email: String): String? {
            return email.exchangeToken()
        }

        override fun invalid(email: String, token: String): Boolean {
            return token.isEmpty() || email.isEmpty() || token != email.exchangeToken()
        }

        override fun save(email: String, token: String) {
            stringRedisTemplate.opsForValue().set(email.toMainKey(), token)
        }
    }

}