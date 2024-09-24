package com.rtomyj.skc.config

import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Component
class CORSConfig : WebFluxConfigurer {
  override fun addCorsMappings(registry: CorsRegistry) {
    registry
        .addMapping("/**")
        .allowedOrigins(
          "http://localhost:3000",
          "https://thesupremekingscastle.com",
          "https://www.thesupremekingscastle.com",
          "https://dev.thesupremekingscastle.com"
        )
        .allowedMethods("GET")
        .maxAge(21600) // 6 hours
  }
}