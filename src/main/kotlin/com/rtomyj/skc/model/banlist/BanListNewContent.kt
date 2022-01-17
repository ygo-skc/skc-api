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

@JsonPropertyOrder("listRequested", "comparedTo", "newCards")
@ApiModel(
    description = "Cards added to requested ban list that were not in the previous ban list and/or cards that have a different ban list status (forbidden, limited, semi-limited) compared to the previous ban list.",
    parent = RepresentationModel::class
)
@Open
data class BanListNewContent(
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
        value = "List containing newly forbidden cards and their previous ban status.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val newForbidden: List<CardsPreviousBanListStatus>,

    @ApiModelProperty(
        value = "List containing newly limited cards and their previous ban status.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val newLimited: List<CardsPreviousBanListStatus>,

    @ApiModelProperty(
        value = "List containing newly semi-limited cards and their previous ban status.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val newSemiLimited: List<CardsPreviousBanListStatus>,

    @ApiModelProperty(
        value = "Total new forbidden cards added to a ban list when compared to a previous logical ban list.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val numNewForbidden: Int = newForbidden.size,

    @ApiModelProperty(
        value = "Total new limited cards added to a list when compared to a previous logical ban list.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val numNewLimited: Int = newLimited.size,

    @ApiModelProperty(
        value = "Total new semi-limited cards added to a ban list when compared to a previous logical ban list.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
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
        HateoasLinks.setLinks(newForbidden)
        HateoasLinks.setLinks(newLimited)
        HateoasLinks.setLinks(newSemiLimited)
    }
}