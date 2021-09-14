package com.rtomyj.skc.config;

import com.rtomyj.skc.enums.SwaggerTagEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
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
						.apis(RequestHandlerSelectors.basePackage("com.rtomyj.yugiohAPI"))
						.paths(PathSelectors.any())
						.build()
				.apiInfo(infoSection())
				.tags(new Tag(SwaggerTagEnum.TEST_CALL_TAG.getTagName(), SwaggerTagEnum.TEST_CALL_TAG.getTagDescription()))
				.tags(new Tag(SwaggerTagEnum.BAN_LIST_TAG.getTagName(), SwaggerTagEnum.BAN_LIST_TAG.getTagDescription()))
				.tags(new Tag(SwaggerTagEnum.CARD_TAG.getTagName(), SwaggerTagEnum.CARD_TAG.getTagDescription()))
				.tags(new Tag(SwaggerTagEnum.PRODUCT_TAG.getTagName(), SwaggerTagEnum.PRODUCT_TAG.getTagDescription()))
				.tags(new Tag(SwaggerTagEnum.STATISTICS_TAG.getTagName(), SwaggerTagEnum.STATISTICS_TAG.getTagDescription()));
	}


	/**
	 * Create and returns an object that has information about me.
	 * @return info about me. Will be used by swagger to display contact information and licenses.
	 */
	private ApiInfo infoSection()
	{

		return new ApiInfoBuilder()
				.title("YuGiOh API")
				.description("Application Programming Interface for interfacing with a Database that contains information such as Ban List dates/content, Yu-Gi-Oh! product information, card information, etc.")
				.version("v1.2.4")
				.termsOfServiceUrl("Terms")
				.contact(new Contact("Javi Gomez", "https://github.com/rtomyj", "rtomyj@gmail.com"))
				.license("Apache License Version 2.0")
				.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.build();

	}

}