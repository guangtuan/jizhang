package tech.igrant.jizhang.framework

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import tech.igrant.jizhang.user.User

@Configuration
class RedisTemplateConfig {

    @Autowired
    lateinit var jedis: RedisConnectionFactory

    @Bean
    fun userDao(): RedisTemplate<String, User> {
        return template(User::class.java)
    }

    private fun <T> template(t: Class<T>): RedisTemplate<String, T> {
        val template = RedisTemplate<String, T>()
        template.setConnectionFactory(jedis)
        template.keySerializer = template.stringSerializer
        val serializer = Jackson2JsonRedisSerializer(t)
        val mapper = ObjectMapper()
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        serializer.setObjectMapper(mapper)
        template.valueSerializer = serializer
        return template
    }

}