package com.rtomyj.skc.model

import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.RequestParam

@ParameterObject
data class CardSearchParameters(
  @Parameter(
    description = SwaggerConstants.CARD_ID_DESCRIPTION, example = "5", schema = Schema(implementation = Int::class)
  ) @RequestParam(name = "cId", required = false, defaultValue = "") val cId: String = "",
  @Parameter(
    description = SwaggerConstants.CARD_NAME_DESCRIPTION,
    example = "hero",
    schema = Schema(implementation = String::class)
  ) @RequestParam(name = "cName", required = false, defaultValue = "") val cName: String = "",
  @Parameter(
    description = SwaggerConstants.CARD_ATTRIBUTE_DESCRIPTION,
    example = "water",
    schema = Schema(implementation = String::class)
  ) @RequestParam(name = "cAttribute", required = false, defaultValue = "") val cAttribute: String = "",
  @Parameter(ref = "cardColor") @RequestParam(name = "cColor",
    required = false,
    defaultValue = "") val cColor: String = "",
  @Parameter(
    description = SwaggerConstants.MONSTER_TYPE_DESCRIPTION,
    example = "war",
    schema = Schema(implementation = String::class)
  ) @RequestParam(name = "mType", required = false, defaultValue = "") val mType: String = "",
  @Parameter(
    description = SwaggerConstants.RESULT_LIMIT_DESCRIPTION,
    example = "10",
    schema = Schema(implementation = Int::class, defaultValue = "5")
  ) @RequestParam(name = "limit", required = false, defaultValue = "5") val limit: Int = 5,
  @Parameter(
    description = SwaggerConstants.SAVE_BANDWIDTH_DESCRIPTION,
    example = "false",
    schema = Schema(implementation = Boolean::class, defaultValue = "false")
  ) @RequestParam(name = "saveBandwidth", required = false, defaultValue = "true") val saveBandwidth: Boolean = true
)