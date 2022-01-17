package com.rtomyj.skc.model.product

import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.product.ProductsController
import com.rtomyj.skc.enums.ProductType
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(description = "Contains information about Yu-Gi-Oh! products.", parent = RepresentationModel::class)
data class Products(
    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val locale: String
) : RepresentationModel<Products?>(), HateoasLinks {

    companion object {
        private val controllerClass = ProductsController::class.java
    }


    @ApiModelProperty(value = "List of Yu-Gi-Oh! products.", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    var products: List<Product>? = null

    @ApiModelProperty(
        value = SwaggerConstants.PRODUCT_TYPE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
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