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
class YugiohAPIMain


/**
 * Inits the program. Main method for program.
 * @param args Java arguments
 */
fun main() {
    SpringApplication.run(YugiohAPIMain::class.java)
}
