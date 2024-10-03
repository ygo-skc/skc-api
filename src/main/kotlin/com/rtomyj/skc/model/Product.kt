package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@JsonInclude(
  JsonInclude.Include.NON_EMPTY
)
data class Product(
  @Schema(ref = "productID")
  val productId: String,
  @Schema(ref = "locale")
  val productLocale: String) {
  @Schema(implementation = String::class, description = "Full product name.")
  var productName: String? = null

  @Schema(ref = "productType")
  var productType: String? = null

  @Schema(
    implementation = String::class,
    description = SwaggerConstants.PRODUCT_SUB_TYPE_DESCRIPTION,
  )
  var productSubType: String? = null

  @Schema(
    implementation = Date::class,
    description = SwaggerConstants.PRODUCT_RELEASE_DATE_DESCRIPTION,
    pattern = "yyyy-MM-dd",
    format = "yyyy-MM-dd",
  )
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  var productReleaseDate: Date? = null

  @Schema(
    implementation = Int::class,
    description = SwaggerConstants.PRODUCT_TOTAL_DESCRIPTION,
  )
  var productTotal: Int? = null

  @Schema(
    implementation = Map::class,
    description = SwaggerConstants.PRODUCT_RARITY_STATS_DESCRIPTION,
  )
  var productRarityStats: Map<String, Int>? = null

  @Schema(
    implementation = MutableList::class,
    description = SwaggerConstants.PRODUCT_CONTENT_DESCRIPTION,
  )
  var productContent: MutableList<ProductContent> = mutableListOf()
}