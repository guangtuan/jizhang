package tech.igrant.jizhang.framework

import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class MVCConfig : WebMvcConfigurerAdapter() {

    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        for (it in converters) {
            if (it is MappingJackson2HttpMessageConverter) {
                it.objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                break
            }
        }
    }

}