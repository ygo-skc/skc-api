package com.rtomyj.skc.util

import org.springframework.beans.factory.annotation.Value

abstract class YgoApiBaseController {
  /**
   * Base endpoint for the API.
   */
  @Value("\${spring.webflux.base-path}")
  var baseEndpoint: String? = null
}