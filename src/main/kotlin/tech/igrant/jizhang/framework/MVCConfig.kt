package tech.igrant.jizhang.framework

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import tech.igrant.jizhang.ext.FMT_YYYY_MM_dd_HH_mm_ss
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Configuration
class MVCConfig : WebMvcConfigurerAdapter() {

    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        for (it in converters) {
            if (it is MappingJackson2HttpMessageConverter) {
                JavaTimeModule().apply {
                    this.addSerializer(LocalDateTime::class.java, LocalDateTimeJsonSerializer())
                    it.objectMapper.registerModule(this)
                }
                break
            }
        }
    }

    class LocalDateTimeJsonSerializer : JsonSerializer<LocalDateTime>() {
        override fun serialize(p0: LocalDateTime?, p1: JsonGenerator?, p2: SerializerProvider?) {
            p1?.writeString(p0?.format(DateTimeFormatter.ofPattern(FMT_YYYY_MM_dd_HH_mm_ss)));
        }
    }

}