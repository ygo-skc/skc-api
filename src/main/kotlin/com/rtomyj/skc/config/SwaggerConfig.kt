package com.rtomyj.skc.config

import com.rtomyj.skc.exception.SKCError
import com.rtomyj.skc.util.constant.AppConstants
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

/**
 * Configuration class for Swagger UI
 */
@Configuration
class SwaggerConfig {
  companion object {
    val SKC_ERROR_PROPERTIES = mapOf("message" to Schema<String>().type("string")
        .description("Error message"),
      "code" to Schema<String>().type("string")
          .description("Error code"))
  }

  /**
   * Create and returns an object that has information about me.
   * @return info about me. Will be used by swagger to display contact information and licenses.
   */
  @Bean
  fun infoSection(): OpenAPI {
    return OpenAPI()
        .info(
          Info()
              .title("SKC API")
              .description("Application Programming Interface (or API for short) for interfacing with a Database that contains information such as Ban List dates/content, Yu-Gi-Oh! product information, card information, etc.")
              .version(AppConstants.APP_VERSION)
              .license(
                License()
                    .name("Apache License Version 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")
              )
              .termsOfService("https://github.com/ygo-skc/skc-api#readme")
        )
        .components(components())
        .externalDocs(
          ExternalDocumentation()
              .description("GitHub")
              .url("https://github.com/ygo-skc/skc-api")
        )
  }

  private fun components() = Components()
      .addResponses("Bad Request", ApiResponse()
          .description("Malformed request. Make sure request is valid JSON and data is using correct data types.")
          .content(Content().addMediaType(APPLICATION_JSON_VALUE,
            MediaType().schema(skcErrorSchema(SKCError(message = "Bad Request", code = "XXX"))))))
      .addResponses("Not Found", ApiResponse()
          .description("No resource found for requested item.")
          .content(Content().addMediaType(APPLICATION_JSON_VALUE,
            MediaType()
                .schema(skcErrorSchema(SKCError(message = "Requested resource was not found", code = "DB001"))))))
      .addResponses("Unprocessable Entity", ApiResponse()
          .description("Request is using data that is wrong - for example using a card ID with 7 digits instead of 8.")
          .content(Content().addMediaType(APPLICATION_JSON_VALUE,
            MediaType()
                .schema(skcErrorSchema(SKCError(message = "URL parameter, URL path parameter or data in body is not valid", code = "G001"))))))
      .addResponses("Internal Server Error", ApiResponse()
          .description("Server encountered an exception.")
          .content(Content().addMediaType(APPLICATION_JSON_VALUE,
            MediaType()
                .schema(skcErrorSchema(SKCError(message = "Error occurred calling service", code = "DS001"))))))

  private fun skcErrorSchema(example: SKCError) = Schema<SKCError>()
      .type("object")
      .description("Error response")
      .properties(SKC_ERROR_PROPERTIES)
      .example(example)
}