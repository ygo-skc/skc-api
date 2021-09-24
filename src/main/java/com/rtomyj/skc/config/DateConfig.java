package com.rtomyj.skc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.text.SimpleDateFormat;

@Configuration
@Lazy
public class DateConfig
{
	@Bean("dbSimpleDateFormat")
	public static SimpleDateFormat getDBSimpleDateFormat()
	{
		return new SimpleDateFormat("yyyy-MM-dd");
	}
}