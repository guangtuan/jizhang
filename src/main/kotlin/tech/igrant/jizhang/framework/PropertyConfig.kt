package tech.igrant.jizhang.framework

import com.alibaba.druid.pool.DruidDataSource
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.connection.RedisPassword
import java.util.*
import javax.sql.DataSource
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory




@Configuration
class PropertyConfig {

    companion object {

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
        fun dataSource(): DataSource {
            val dataSource = DruidDataSource()
            val dbHost = System.getenv("JIZHANG_DB_HOST")
            val dbName = System.getenv("JIZHANG_DB_NAME")
            val dbUser = System.getenv("JIZHANG_DB_USER")
            val dbPassword = System.getenv("JIZHANG_DB_PASSWORD")
            dataSource.url = dataSourceUrl(dbHost, dbName)
            dataSource.username = dbUser
            dataSource.password = dbPassword
            return dataSource
        }

        @Bean
        fun propertyConfigure(): PropertyPlaceholderConfigurer {
            val placeholderConfigurerSupport = PropertyPlaceholderConfigurer()
            val dbHost = System.getenv("JIZHANG_DB_HOST")
            val dbName = System.getenv("JIZHANG_DB_NAME")
            val dbUser = System.getenv("JIZHANG_DB_USER")
            val dbPassword = System.getenv("JIZHANG_DB_PASSWORD")
            val properties = Properties()
            properties["spring.datasource.url"] = dataSourceUrl(dbHost, dbName)
            properties["spring.datasource.dbname"] = dbName
            properties["spring.datasource.username"] = dbUser
            properties["spring.datasource.password"] = dbPassword
            placeholderConfigurerSupport.setProperties(properties)
            val appProperties = ClassPathResource("application.properties")
            val druidProperties = ClassPathResource("druid.properties")
            placeholderConfigurerSupport.setLocations(appProperties, druidProperties)
            return placeholderConfigurerSupport
        }

        private fun dataSourceUrl(dbHost: String, dbName: String): String {
            return "jdbc:mysql://$dbHost/$dbName?useUnicode=true&characterEncoding=utf-8&useSSL=false"
        }

    }

}