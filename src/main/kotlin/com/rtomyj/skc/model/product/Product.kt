package com.rtomyj.skc.model.product

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.product.ProductController
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import java.util.*
import java.util.function.Consumer

@JsonInclude(
    JsonInclude.Include.NON_EMPTY
)
@ApiModel(description = "Information about a Yu-Gi-Oh! product such as a booster pack or tin.")
data class Product(
    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_ID_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val productId: String,
    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val productLocale: String
) : RepresentationModel<Product?>() {

    companion object {
        private val controllerClass = ProductController::class.java
        fun setLinks(products: List<Product>) {
            products
                .forEach(Consumer { obj: Product -> obj.setLinks() })
        }
    }


    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_NAME_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    var productName: String? = null

    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_TYPE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    var productType: String? = null

    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_SUB_TYPE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    var productSubType: String? = null

    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_RELEASE_DATE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var productReleaseDate: Date? = null

    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_TOTAL_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    var productTotal: Int? = null

    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_RARITY_STATS_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    var productRarityStats: Map<String, Int>? = null

    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_CONTENT_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    var productContent: MutableList<ProductContent> = mutableListOf()


    private fun setLink() {
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(controllerClass).productInfo(
                    productId, productLocale
                )
            ).withSelfRel()
        )
    }

    fun setLinks() {
        setLink()
        if (productContent.isNotEmpty()) ProductContent.setLinks(productContent) // set links for pack contents
    }
}