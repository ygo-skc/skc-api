package com.rtomyj.skc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
class AsyncConfig {
	@Bean("asyncExecutor")
	fun asyncExecutor(): Executor {
		val executor = ThreadPoolTaskExecutor()

		executor.corePoolSize = 15
		executor.maxPoolSize = 15
		executor.setQueueCapacity(75)
		executor.setThreadNamePrefix("AsyncThread-")
		executor.initialize()

		return executor
	}
}