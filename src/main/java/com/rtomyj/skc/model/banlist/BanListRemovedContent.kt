package com.rtomyj.skc.model.banlist

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.rtomyj.skc.Open
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.banlist.BanListDiffController
import com.rtomyj.skc.controller.banlist.BannedCardsController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@JsonPropertyOrder("listRequested", "comparedTo", "numRemoved", "removedCards")
@ApiModel(
    description = "Cards that were removed from a ban list compared to the previous logical ban list.",
    parent = RepresentationModel::class
)
@Open
data class BanListRemovedContent(
    @ApiModelProperty(
        value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val listRequested: String,

    @ApiModelProperty(
        value = SwaggerConstants.PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val comparedTo: String,

    @ApiModelProperty(
        value = "List containing removed cards and their previous ban status.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val removedCards: List<CardsPreviousBanListStatus>,

    @ApiModelProperty(
        value = "Total number of cards removed in requested ban list compared to previous ban list.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
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
        HateoasLinks.setLinks(removedCards)
    }
}