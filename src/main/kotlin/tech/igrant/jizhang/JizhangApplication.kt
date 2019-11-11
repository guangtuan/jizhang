package tech.igrant.jizhang

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("tech.igrant")
@ServletComponentScan
class JizhangApplication

fun main(args: Array<String>) {
    runApplication<JizhangApplication>(*args)
}
