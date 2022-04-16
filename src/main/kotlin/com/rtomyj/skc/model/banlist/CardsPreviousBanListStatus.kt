package com.rtomyj.skc.model.banlist

import com.rtomyj.skc.Open
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.card.CardController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@Schema(
    implementation = CardsPreviousBanListStatus::class,
    description = "Contains info about a cards' previous ban status (forbidden, limited, semi-limited) that changed in reference to a previous logical ban list.",
)
@Open
data class CardsPreviousBanListStatus(
    @Schema(
        implementation = CardsPreviousBanListStatus::class,
        description = SwaggerConstants.CARD_ID_DESCRIPTION, 
    )
    val cardId: String,

    @Schema(
        implementation = CardsPreviousBanListStatus::class,
        description = SwaggerConstants.CARD_NAME_DESCRIPTION,
    )
    val cardName: String,

    @Schema(
        implementation = CardsPreviousBanListStatus::class,
        description = "The previous ban status the card had when compared to current ban list.",
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