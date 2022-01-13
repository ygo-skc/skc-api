package com.rtomyj.skc.config

import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spi.DocumentationType
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.PathSelectors
import com.rtomyj.skc.enums.SwaggerTagEnum
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.service.ApiInfo
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.service.Contact
import springfox.documentation.service.Tag

/**
 * Configuration class for Swagger UI
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {
	/**
	 * Configures Swagger by specifying the package API controllers are stored.
	 * @return bean with Swagger configuration
	 */
	@Bean
	fun productApi(): Docket {
		return Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.rtomyj.skc"))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(infoSection())
			.tags(Tag(SwaggerTagEnum.TEST_CALL_TAG.tagName, SwaggerTagEnum.TEST_CALL_TAG.tagDescription))
			.tags(Tag(SwaggerTagEnum.BAN_LIST_TAG.tagName, SwaggerTagEnum.BAN_LIST_TAG.tagDescription))
			.tags(Tag(SwaggerTagEnum.CARD_TAG.tagName, SwaggerTagEnum.CARD_TAG.tagDescription))
			.tags(Tag(SwaggerTagEnum.PRODUCT_TAG.tagName, SwaggerTagEnum.PRODUCT_TAG.tagDescription))
			.tags(Tag(SwaggerTagEnum.STATISTICS_TAG.tagName, SwaggerTagEnum.STATISTICS_TAG.tagDescription))
	}

	/**
	 * Create and returns an object that has information about me.
	 * @return info about me. Will be used by swagger to display contact information and licenses.
	 */
	private fun infoSection(): ApiInfo {
		return ApiInfoBuilder()
			.title("SKC API")
			.description("Application Programming Interface (or API for short) for interfacing with a Database that contains information such as Ban List dates/content, Yu-Gi-Oh! product information, card information, etc.")
			.version("v1.3.0")
			.termsOfServiceUrl("https://github.com/ygo-skc/skc-api#readme")
			.contact(Contact("Supreme King", "https://thesupremekingscastle.com/about", "thesupremeking25@gmail.com"))
			.license("Apache License Version 2.0")
			.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
			.build()
	}
}