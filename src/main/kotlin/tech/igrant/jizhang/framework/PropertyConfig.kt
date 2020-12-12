package tech.igrant.jizhang.framework

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import javax.sql.DataSource


@Configuration
class PropertyConfig {

    companion object {

        private val logger: Logger = LoggerFactory.getLogger(PropertyConfig::class.java)

        @Bean
        fun redisConnectionFactory(): LettuceConnectionFactory {
            val redisHost = System.getenv("JIZHANG_CACHE_HOST")
            val redisPort = System.getenv("JIZHANG_CACHE_PORT").toInt()
            val redisPass = System.getenv("JIZHANG_CACHE_PASS")
            val redisDb = System.getenv("JIZHANG_CACHE_DB")?.toInt()
            val redisStandaloneConfiguration = RedisStandaloneConfiguration(redisHost, redisPort)
            redisStandaloneConfiguration.password = RedisPassword.of(redisPass)
            redisStandaloneConfiguration.database = redisDb ?: 0
            return LettuceConnectionFactory(redisStandaloneConfiguration)
        }

        @Bean
        @Primary
        fun dataSource(): DataSource? {
            val dbHost = System.getenv("JIZHANG_DB_HOST")
            val dbName = System.getenv("JIZHANG_DB_NAME")
            val dbUser = System.getenv("JIZHANG_DB_USER")
            val dbPassword = System.getenv("JIZHANG_DB_PASSWORD")
            return DataSourceBuilder
                    .create()
                    .username(dbUser)
                    .password(dbPassword)
                    .url(dataSourceUrl(dbHost, dbName))
                    .driverClassName("com.mysql.jdbc.Driver")
                    .build()
        }

        private fun dataSourceUrl(dbHost: String, dbName: String): String {
            return "jdbc:mysql://$dbHost/$dbName?useUnicode=true&characterEncoding=utf-8&useSSL=false"
        }

    }

}