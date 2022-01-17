package com.rtomyj.skc.model.product

import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.model.card.Card
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import java.util.function.Consumer

@JsonInclude(
    JsonInclude.Include.NON_EMPTY
)
@ApiModel(parent = RepresentationModel::class)
data class ProductContent(
    @ApiModelProperty(value = "Information about card.")
    val card: Card?,
    @ApiModelProperty(value = SwaggerConstants.CARD_POSITION_IN_PRODUCT_DESCRIPTION)
    val productPosition: String,
    @ApiModelProperty(value = SwaggerConstants.CARD_RARITIES_FOR_POSITION_DESCRIPTION)
    val rarities: Set<String>
) : RepresentationModel<ProductContent?>() {

    companion object {
        fun setLinks(packContents: List<ProductContent>) {
            packContents
                .forEach(Consumer { obj: ProductContent -> obj.setLinks() })
        }
    }
    

    fun setLinks() {
        card?.setLinks()
    }
}