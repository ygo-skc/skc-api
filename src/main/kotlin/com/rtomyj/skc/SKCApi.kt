package com.rtomyj.skc

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@OpenAPIDefinition
class SKCApi

fun main() {
    // must be set before Reactor's Schedulers class loads
    System.setProperty("reactor.schedulers.defaultBoundedElasticOnVirtualThreads", "true")
    SpringApplication.run(SKCApi::class.java)
}
