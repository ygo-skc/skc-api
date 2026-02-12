package com.rtomyj.skc

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@OpenAPIDefinition
class SKCApi

fun main() {
  SpringApplication.run(SKCApi::class.java)
}
