package com.rtomyj.skc.model

import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.RequestParam

@ParameterObject
data class CardSearchParameters(
  @field:Parameter(
    description = SwaggerConstants.CARD_ID_DESCRIPTION, example = "5", schema = Schema(implementation = Int::class)
  ) @RequestParam(name = "cId", required = false, defaultValue = "") val cId: String = "",
  @field:Parameter(
    description = SwaggerConstants.CARD_NAME_DESCRIPTION,
    example = "hero",
    schema = Schema(implementation = String::class)
  ) @RequestParam(name = "cName", required = false, defaultValue = "") val cName: String = "",
  @field:Parameter(
    description = SwaggerConstants.RESULT_LIMIT_DESCRIPTION,
    example = "10",
    schema = Schema(implementation = Int::class, defaultValue = "5")
  ) @RequestParam(name = "limit", required = false, defaultValue = "5") val limit: Int = 5,
  @field:Parameter(
    description = SwaggerConstants.SAVE_BANDWIDTH_DESCRIPTION,
    example = "false",
    schema = Schema(implementation = Boolean::class, defaultValue = "false")
  ) @RequestParam(name = "saveBandwidth", required = false, defaultValue = "true") val saveBandwidth: Boolean = true
)