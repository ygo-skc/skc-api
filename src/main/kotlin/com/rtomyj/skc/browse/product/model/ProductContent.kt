package com.rtomyj.skc.browse.product.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.browse.card.model.Card
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import java.util.function.Consumer

@JsonInclude(
    JsonInclude.Include.NON_EMPTY
)
@Schema(
    implementation = Products::class,
)
data class ProductContent(
	@Schema(
        implementation = Card::class,
        description = "Information about card."
    )
    val card: Card?,
	@Schema(
        implementation = String::class,
        description = SwaggerConstants.CARD_POSITION_IN_PRODUCT_DESCRIPTION
    )
    val productPosition: String,
	@Schema(
        implementation = Set::class,
        description = SwaggerConstants.CARD_RARITIES_FOR_POSITION_DESCRIPTION
    )
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