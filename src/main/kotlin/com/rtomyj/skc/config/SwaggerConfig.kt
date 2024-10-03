package com.rtomyj.skc.config

import com.rtomyj.skc.exception.SKCError
import com.rtomyj.skc.util.constant.AppConstants
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter
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
    const val CARD_COLOR_DESCRIPTION =
      "A simple identifier for card type. If the card is synchro, the card color is synchro."
    const val HTTP_200_SWAGGER_MESSAGE = "Request processed successfully."
  }

  /**
   * Create and returns an object that has information about me.
   * @return info about me. Will be used by swagger to display contact information and licenses.
   */
  @Bean
  fun infoSection() = OpenAPI()
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

  private fun components() = Components()
      // examples
      .addExamples("fusion", Example().value("fusion"))
      .addExamples("effect", Example().value("effect"))
      .addExamples("synchro", Example().value("synchro"))
      // schemas
      .addSchemas("serviceStatus",
        Schema<String>().type("string")
            .description("Current status of a service")
            .example("API is online and functional"))
      .addSchemas("serviceVersion",
        Schema<String>().type("string")
            .description("Current version of a service")
            .example("1.2.3"))
      .addSchemas("locale",
        Schema<String>().type("string")
            .example("en"))
      .addSchemas("cardColor",
        Schema<String>().type("string")
            .description(CARD_COLOR_DESCRIPTION)
            .examples(listOf(Example().`$ref`("fusion"), Example().`$ref`("effect"), Example().`$ref`("synchro"))))
      .addSchemas("productID",
        Schema<String>().type("string")
            .example("LOB"))
      .addSchemas("skcError", Schema<SKCError>()
          .type("object")
          .description("Error response")
          .properties(mapOf("message" to Schema<String>().type("string")
              .description("Error message"),
            "code" to Schema<String>().type("string")
                .description("Error code"))))
      .addSchemas("skcErrorBadRequest", Schema<SKCError>().`$ref`("skcError")
          .example(SKCError(message = "Bad Request", code = "XXX")))
      .addSchemas("skcErrorNotFound", Schema<SKCError>().`$ref`("skcError")
          .example(SKCError(message = "Requested resource was not found", code = "DB001")))
      .addSchemas("skcErrorUnprocessableEntity", Schema<SKCError>().`$ref`("skcError")
          .example(SKCError(message = "URL parameter, URL path parameter or data in body is not valid", code = "G001")))
      .addSchemas("skcErrorInternalServerError", Schema<SKCError>().`$ref`("skcError")
          .example(SKCError(message = "Error occurred calling service", code = "DS001")))
      // responses
      .addResponses("badRequest", ApiResponse()
          .description("Malformed request. Make sure request is valid JSON and data is using correct data types.")
          .content(Content().addMediaType(APPLICATION_JSON_VALUE, MediaType().schema(Schema<SKCError>().`$ref`("skcErrorBadRequest")))))
      .addResponses("notFound", ApiResponse()
          .description("No resource found for requested item.")
          .content(Content().addMediaType(APPLICATION_JSON_VALUE, MediaType().schema(Schema<SKCError>().`$ref`("skcErrorNotFound")))))
      .addResponses("unprocessableEntity", ApiResponse()
          .description("Request is using data that is wrong - for example using a card ID with 7 digits instead of 8.")
          .content(Content().addMediaType(APPLICATION_JSON_VALUE, MediaType().schema(Schema<SKCError>().`$ref`("skcErrorUnprocessableEntity")))))
      .addResponses("internalServerError", ApiResponse()
          .description("Server encountered an exception.")
          .content(Content().addMediaType(APPLICATION_JSON_VALUE, MediaType().schema(Schema<SKCError>().`$ref`("skcErrorInternalServerError")))))
      // parameters
      .addParameters("locale", Parameter()
          .description("A locale denoting a geographical region. As of now the only locale available is \"en\" but in the future other locales can be added to support releases in other regions like Japan.")
          .name("locale")
          .required(true)
          .example("en")
          .`in`("path"))
      .addParameters("productID", Parameter()
          .description("Unique identifier each Yu-Gi-Oh! product has. It is the 3 or 4 alpha numeric string found on every card.")
          .name("productId")
          .required(true)
          .example("LOB")
          .`in`("path"))
      .addParameters("productType", Parameter()
          .description("A specific product type used to limit results.")
          .name("productType")
          .required(true)
          .example("PACK")
          .`in`("path"))
}