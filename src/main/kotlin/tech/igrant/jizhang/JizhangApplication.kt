package tech.igrant.jizhang

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("tech.igrant")
@ServletComponentScan
class JizhangApplication : SpringBootServletInitializer() {

    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        return builder.sources(JizhangApplication::class.java)
    }

}

fun main(args: Array<String>) {
    runApplication<JizhangApplication>(*args)
}