package net.falsetrue.stackexchangequerier.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.view.InternalResourceViewResolver



@Configuration
@EnableWebMvc
class MvcConfig: WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
    }

    @Bean
    fun jspViewResolver(): InternalResourceViewResolver {
        val bean = InternalResourceViewResolver()
        bean.setPrefix("/WEB-INF/jsp/")
        bean.setSuffix(".jsp")
        return bean
    }
}
