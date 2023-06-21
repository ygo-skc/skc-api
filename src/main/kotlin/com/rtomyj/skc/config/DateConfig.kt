package com.rtomyj.skc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

@Configuration
@Lazy
class DateConfig {
	@Bean("dbSimpleDateFormat")
	fun dBSimpleDateFormat(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

	@Bean("dbDateTimeFormatter")
	fun dbDateTimeFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
}