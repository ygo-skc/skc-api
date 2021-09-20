package com.rtomyj.skc.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig
{

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor()
    {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15);
        executor.setMaxPoolSize(15);
        executor.setQueueCapacity(75);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;

    }

}
