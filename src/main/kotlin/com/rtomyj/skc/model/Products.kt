package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.util.enumeration.ProductType
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Products(@field:Schema(ref = "locale") val locale: String) {
  @Schema(implementation = List::class, description = "List of Yu-Gi-Oh! products.")
  var products: List<Product>? = null

  @Schema(ref = "productType")
  var productType: ProductType? = null
}