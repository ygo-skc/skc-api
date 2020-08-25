package com.rtomyj.yugiohAPI.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for Swagger UI
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
{

	/**
	 * Configures Swagger by specifying the package API controllers are stored.
	 * @return bean with Swagger configuration
	 */
	@Bean
	public Docket productApi()
	{

		return new Docket(DocumentationType.SWAGGER_2)
			.select()
					.paths(PathSelectors.any())
					.build()
			.apiInfo(infoSection());

	}


	/**
	 * Create and returns an object that has information about me.
	 * @return info about me. Will be used by swagger to display contact information and licenses.
	 */
	private ApiInfo infoSection()
	{

		return new ApiInfoBuilder()
				.title("YuGiOh API")
				.description("Application Programming Interface for interfacing with a Database that contains information such as Ban List dates/content, YuGiOh product information, card information, etc.")
				.version("v1.2")
				.termsOfServiceUrl("Terms")
				.contact(new Contact("Javi Gomez", "https://github.com/rtomyj", "rtomyj@gmail.com"))
				.license("Apache License Version 2.0")
				.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.build();

	}

}