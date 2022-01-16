package com.rtomyj.skc.model.banlist

import com.rtomyj.skc.Open
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.card.CardController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@ApiModel(
    description = "Contains info about a cards' previous ban status (forbidden, limited, semi-limited) that changed in reference to a previous logical ban list.",
    parent = RepresentationModel::class
)
@Open
data class CardsPreviousBanListStatus(
    @ApiModelProperty(value = SwaggerConstants.CARD_ID_DESCRIPTION, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    val cardId: String,

    @ApiModelProperty(
        value = SwaggerConstants.CARD_NAME_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val cardName: String,

    @ApiModelProperty(
        value = "The previous ban status the card had when compared to current ban list.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val previousBanStatus: String
) : RepresentationModel<CardsPreviousBanListStatus?>(), HateoasLinks {

    companion object {
        private val controllerClass = CardController::class.java
    }


    override fun setSelfLink() {
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(controllerClass).getCard(
                    cardId, false
                )
            ).withSelfRel()
        )
    }


    override fun setLinks() {
        setSelfLink()
    }
}