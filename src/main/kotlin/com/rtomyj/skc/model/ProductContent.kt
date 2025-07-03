package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(
  JsonInclude.Include.NON_EMPTY
)
@Schema(
  implementation = Products::class,
)
data class ProductContent(
  @field:Schema(
    implementation = Card::class,
    description = "Information about card."
  )
  val card: Card?,
  @field:Schema(
    implementation = String::class,
    description = SwaggerConstants.CARD_POSITION_IN_PRODUCT_DESCRIPTION
  )
  val productPosition: String,
  @field:Schema(
    implementation = Set::class,
    description = SwaggerConstants.CARD_RARITIES_FOR_POSITION_DESCRIPTION
  )
  val rarities: Set<String>
)