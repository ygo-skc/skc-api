package com.rtomyj.skc

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Contains main method.
 */
@SpringBootApplication
@OpenAPIDefinition
class SKCApi

/**
 * Inits the program. Main method for program.
 */
fun main() {
  SpringApplication.run(SKCApi::class.java)
}
