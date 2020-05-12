package com.rtomyj.yugiohAPI.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.yugiohAPI.model.product.pack.Pack;
import com.rtomyj.yugiohAPI.util.mixin.PackMixin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ObjectMapperConfig
{

	@Bean("objectMapper")
	public ObjectMapper objectMapper()
	{
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.addMixIn(Pack.class, PackMixin.class);

		log.debug("Finalized creation of default Object Mapper");
		return objectMapper;
	}

}