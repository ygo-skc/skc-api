package com.rtomyj.skc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.boot.SpringApplication

/**
 * Contains main method.
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
class SKCApi


/**
 * Inits the program. Main method for program.
 */
fun main() {
    SpringApplication.run(SKCApi::class.java)
}
