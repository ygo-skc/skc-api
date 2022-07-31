package com.rtomyj.skc.banlist.model

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.rtomyj.skc.Open
import com.rtomyj.skc.banlist.controller.BanListDiffController
import com.rtomyj.skc.banlist.controller.BannedCardsController
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.util.HateoasLinks
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@JsonPropertyOrder("listRequested", "comparedTo", "numRemoved", "removedCards")
@Schema(
    implementation = BanListRemovedContent::class,
    description = "Cards that were removed from a ban list compared to the previous logical ban list.",
)
@Open
data class BanListRemovedContent(
	@Schema(
        implementation = String::class,
        description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
    )
    val listRequested: String,

	@Schema(
        implementation = String::class,
        description = SwaggerConstants.PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION,
    )
    val comparedTo: String,

	@Schema(
        implementation = List::class,
        description = "List containing removed cards and their previous ban status.",
    )
    val removedCards: List<CardsPreviousBanListStatus>,

	@Schema(
        implementation = Int::class,
        description = "Total number of cards removed in requested ban list compared to previous ban list.",
    )
    val numRemoved: Int = removedCards.size
) : RepresentationModel<BanListRemovedContent?>(), HateoasLinks {

    companion object {
        private val banListController = BannedCardsController::class.java
        private val diffController = BanListDiffController::class.java
    }


    override fun setSelfLink() {
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(diffController).getNewlyRemovedContentForBanList(listRequested)
            ).withSelfRel()
        )
    }

    override fun setLinks() {
        setSelfLink()

        removedCards
            .forEach(CardsPreviousBanListStatus::setLinks)

        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(banListController).getBannedCards(listRequested, false, true)
            ).withRel("Ban List Content")
        )
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(diffController).getNewlyAddedContentForBanList(listRequested)
            ).withRel("Ban List New Content")
        )
    }
}