package com.rtomyj.skc.banlist.model

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.rtomyj.skc.Open
import com.rtomyj.skc.banlist.controller.BanListDiffController
import com.rtomyj.skc.banlist.controller.BannedCardsController
import com.rtomyj.skc.util.constant.SwaggerConstants
import com.rtomyj.skc.util.HateoasLinks
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@JsonPropertyOrder("listRequested", "comparedTo", "newCards")
@Schema(
    implementation = BanListNewContent::class,
    description = "Cards added to requested ban list that were not in the previous ban list and/or cards that have a different ban list status (forbidden, limited, semi-limited) compared to the previous ban list.",
)
@Open
data class BanListNewContent(
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
        description = "List containing newly forbidden cards and their previous ban status.",
    )
    val newForbidden: List<CardsPreviousBanListStatus>,

	@Schema(
        implementation = List::class,
        description = "List containing newly limited cards and their previous ban status.",
    )
    val newLimited: List<CardsPreviousBanListStatus>,

	@Schema(
        implementation = List::class,
        description = "List containing newly semi-limited cards and their previous ban status.",
    )
    val newSemiLimited: List<CardsPreviousBanListStatus>,

	@Schema(
        implementation = Int::class,
        description = "Total new forbidden cards added to a ban list when compared to a previous logical ban list.",
    )
    val numNewForbidden: Int = newForbidden.size,

	@Schema(
        implementation = Int::class,
        description = "Total new limited cards added to a list when compared to a previous logical ban list.",
    )
    val numNewLimited: Int = newLimited.size,

	@Schema(
        implementation = Int::class,
        description = "Total new semi-limited cards added to a ban list when compared to a previous logical ban list.",
    )
    val numNewSemiLimited: Int = newSemiLimited.size
) : RepresentationModel<BanListNewContent?>(), HateoasLinks {

    companion object {
        private val BAN_LIST_DIFF_CONTROLLER_CLASS = BanListDiffController::class.java
        private val banListController = BannedCardsController::class.java
    }


    override fun setSelfLink() {
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS).getNewlyAddedContentForBanList(listRequested)
            ).withSelfRel()
        )
    }


    override fun setLinks() {
        setSelfLink()
        newForbidden
            .forEach(CardsPreviousBanListStatus::setLinks)
        newLimited
            .forEach(CardsPreviousBanListStatus::setLinks)
        newSemiLimited
            .forEach(CardsPreviousBanListStatus::setLinks)

        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(banListController).getBannedCards(listRequested, false, true)
            ).withRel("Ban List Content")
        )
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS)
                    .getNewlyRemovedContentForBanList(listRequested)
            ).withRel("Ban List Removed Content")
        )
    }
}