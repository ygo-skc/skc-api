package com.rtomyj.skc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import java.text.SimpleDateFormat

@Configuration
@Lazy
class DateConfig {
	@Bean("dbSimpleDateFormat")
	fun dBSimpleDateFormat(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
}