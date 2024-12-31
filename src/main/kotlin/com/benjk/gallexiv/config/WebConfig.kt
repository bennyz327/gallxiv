package com.benjk.gallexiv.config

import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus

@Configuration
class WebConfig {
    @Bean
    fun webServerFactoryCustomizer(): WebServerFactoryCustomizer<ConfigurableWebServerFactory>? {
        return WebServerFactoryCustomizer { factory: ConfigurableWebServerFactory ->
            val error404Page = ErrorPage(HttpStatus.NOT_FOUND, "/")
            factory.addErrorPages(error404Page)
        }
    }
}