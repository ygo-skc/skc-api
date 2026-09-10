package com.rtomyj.skc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import java.time.format.DateTimeFormatter

@Configuration
@Lazy
class DateConfig {
    @Bean("dbDateTimeFormatter")
    fun dbDateTimeFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
}
