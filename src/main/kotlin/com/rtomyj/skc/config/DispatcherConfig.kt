package com.rtomyj.skc.config

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class DispatcherConfig {
    @Bean("jdbc-dispatcher")
    fun jdbcDispatcher(
        @Value("\${spring.datasource.hikari.maximumPoolSize:30}") poolSize: Int,
    ): CoroutineDispatcher =
        Executors
            .newThreadPerTaskExecutor(Thread.ofVirtual().name("jdbc-", 0).factory())
            .asCoroutineDispatcher()
            .limitedParallelism(poolSize, "jdbc")
}
