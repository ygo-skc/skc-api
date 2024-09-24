package com.rtomyj.skc.config

import com.rtomyj.skc.util.constant.AppConstants
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for Swagger UI
 */
@Configuration
class SwaggerConfig {

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
        .externalDocs(
          ExternalDocumentation()
              .description("GitHub")
              .url("https://github.com/ygo-skc/skc-api")
        )
  }
}