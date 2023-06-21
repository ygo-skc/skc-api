package com.rtomyj.skc

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.*

/**
 * Contains main method.
 */
@SpringBootApplication
class SKCApi {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)
        private const val timeZone = "America/Chicago"
    }

    @PostConstruct
    fun init() {
        // Setting Spring Boot SetTimeZone
        log.info("Configuring API timezone as {}", timeZone)
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone))
    }
}

/**
 * Inits the program. Main method for program.
 */
fun main() {
    SpringApplication.run(SKCApi::class.java)
}
