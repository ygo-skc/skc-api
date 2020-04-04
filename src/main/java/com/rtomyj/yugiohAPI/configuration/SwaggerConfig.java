package com.rtomyj.yugiohAPI.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Configuration class for Swagger UI
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

	/**
	 * Configures Swagger by specifying the package API controllers are stored.
	 * @return bean with Swagger configuration
	 */
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.rtomyj.yugiohAPI"))
			.build()
			.apiInfo(infoSection());
	}



	/**
	 * Create and returns an object that has information about me.
	 * @return info about me. Will be used by swagger to display contact information and licenses.
	 */
	private ApiInfo infoSection()
	{
		return new ApiInfo("Yugioh API", "Desc", "v1.0", "Terms", new Contact("Javi Gomez", "https://github.com/rtomyj", "rtomyj@gmail.com")
		, "Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0", new ArrayList<>());
	}
}