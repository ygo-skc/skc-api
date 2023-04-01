package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.browse.product.ProductBrowseController
import com.rtomyj.skc.util.HateoasLinks
import com.rtomyj.skc.util.constant.SwaggerConstants
import com.rtomyj.skc.util.enumeration.ProductType
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(
    implementation = Products::class,
    description = "Contains information about Yu-Gi-Oh! products."
)
data class Products(
    @Schema(
        implementation = String::class,
        description = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
    )
    val locale: String
) : RepresentationModel<Products?>(), HateoasLinks {

    companion object {
        private val controllerClass = ProductBrowseController::class.java
    }


    @Schema(
        implementation = Product::class,
        description = "List of Yu-Gi-Oh! products."
    )
    var products: List<Product>? = null

    @Schema(
        implementation = ProductType::class,
        description = SwaggerConstants.PRODUCT_TYPE_DESCRIPTION,
    )
    var productType: ProductType? = null


    override fun setSelfLink() {
        if (productType == null) {
            this.add(
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(controllerClass)
                        .getProductsByLocale(locale)
                )
                    .withSelfRel()
            )
        } else {
            this.add(
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(controllerClass)
                        .getAllProductsForProductTypeAndLocale(productType!!, locale)
                )
                    .withSelfRel()
            )
        }
    }


    override fun setLinks() {
        setSelfLink()
        Product.setLinks(products!!)
    }
}