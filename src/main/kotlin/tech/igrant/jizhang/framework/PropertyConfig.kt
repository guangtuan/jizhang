package tech.igrant.jizhang.framework

import com.alibaba.druid.pool.DruidDataSource
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.util.*
import javax.sql.DataSource


@Configuration
class PropertyConfig {

    companion object {

        @Bean
        fun dataSource(): DataSource {
            val dataSource = DruidDataSource()
            val dbHost = System.getenv("JIZHANG_DB_HOST")
            val dbName = System.getenv("JIZHANG_DB_NAME")
            val dbUser = System.getenv("JIZHANG_DB_USER")
            val dbPassword = System.getenv("JIZHANG_DB_PASSWORD")
            val redisUrl = System.getenv("JIZHANG_REDIS_URL")
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
            val redisUrl = System.getenv("JIZHANG_REDIS_URL")
            val properties = Properties()
            properties["spring.datasource.url"] = dataSourceUrl(dbHost, dbName)
            properties["spring.datasource.dbname"] = dbName
            properties["spring.datasource.username"] = dbUser
            properties["spring.datasource.password"] = dbPassword
            properties["spring.redis.host"] = redisUrl
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