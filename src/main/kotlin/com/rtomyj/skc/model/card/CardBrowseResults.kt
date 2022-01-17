package com.rtomyj.skc.model.card

import com.rtomyj.skc.controller.card.CardBrowseController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel

@ApiModel(
    description = "Card information for cards that fit requested browse criteria.",
    parent = RepresentationModel::class
)
class CardBrowseResults(
    @ApiModelProperty(
        value = "Card info of all cards that fit criteria requested by user.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val results: List<Card>,
    @ApiModelProperty(value = "Total browse results.", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    val numResults: Int = 0
) : RepresentationModel<CardBrowseResults>(), HateoasLinks {

    companion object {
        private val cardBrowseController = CardBrowseController::class.java
    }


    @ApiModelProperty(
        value = "Criteria used to fetch these results.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    var requestedCriteria: CardBrowseCriteria? = null


    // TODO: add links
    override fun setSelfLink() {}


    override fun setLinks() {
        setSelfLink()
        HateoasLinks.setLinks(results)
    }
}