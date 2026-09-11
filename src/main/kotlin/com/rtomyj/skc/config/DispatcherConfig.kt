package com.rtomyj.skc.config

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DispatcherConfig {
    @Bean("jdbc-dispatcher")
    fun jdbcDispatcher(
        @Value("\${spring.datasource.hikari.maximumPoolSize:30}") poolSize: Int,
    ): CoroutineDispatcher = Dispatchers.IO.limitedParallelism(poolSize, "jdbc")
}
