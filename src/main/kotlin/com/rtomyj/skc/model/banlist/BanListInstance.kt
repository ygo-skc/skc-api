package com.rtomyj.skc.model.banlist

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.rtomyj.skc.Open
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.banlist.BanListDiffController
import com.rtomyj.skc.controller.banlist.BannedCardsController
import com.rtomyj.skc.model.HateoasLinks
import com.rtomyj.skc.model.card.Card
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@JsonTypeName("banListInstance")
@JsonPropertyOrder(
    "effectiveDate",
    "comparedTo",
    "numForbidden",
    "numLimited",
    "numSemiLimited",
    "forbidden",
    "limited",
    "semiLimited"
)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(
    description = "Describes and contains information about a specific ban list.",
    parent = RepresentationModel::class
)
@Open
data class BanListInstance(
    @ApiModelProperty(
        value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val effectiveDate: String,

    @ApiModelProperty(
        value = SwaggerConstants.PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val comparedTo: String,


    @ApiModelProperty(
        value = "List of cards forbidden in this ban list instance.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY,
        position = 4
    )
    val forbidden: List<Card>,

    @ApiModelProperty(
        value = "List of cards limited in this ban list instance.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY,
        position = 5
    )
    val limited: List<Card>,

    @ApiModelProperty(
        value = "List of cards semi-limited in this ban list instance.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY,
        position = 6
    )
    val semiLimited: List<Card>,

    @ApiModelProperty(
        value = "Total number of cards forbidden in this ban list instance; ie size of forbidden list.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY,
        position = 1
    )
    val numForbidden: Int = forbidden.size,

    @ApiModelProperty(
        value = "Total number of cards limited in this ban list instance; ie size of limited list.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY,
        position = 2
    )
    val numLimited: Int = limited.size,

    @ApiModelProperty(
        value = "Total number of cards semi-limited in this ban list instance; ie size of semi-limited list.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY,
        position = 3
    )
    val numSemiLimited: Int = semiLimited.size
) : RepresentationModel<BanListInstance>(), HateoasLinks {


    companion object {
        private val banListController = BannedCardsController::class.java
        private val BAN_LIST_DIFF_CONTROLLER_CLASS = BanListDiffController::class.java
    }


    @ApiModelProperty(
        value = "Object containing info of cards that are newly added to this ban list compared to previous logical ban list. Note: this field will be null unless specified otherwise.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY,
        position = 7
    )
    var newContent: BanListNewContent? = null

    @ApiModelProperty(
        value = "Object containing info of cards that are removed from this ban list compared to previous logical ban list. Note: this field will be null unless specified otherwise.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY,
        position = 8
    )
    var removedContent: BanListRemovedContent? = null


    override fun setSelfLink() {
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(banListController).getBannedCards(effectiveDate, false, true)
            )
                .withSelfRel()
        )
    }


    override fun setLinks() {
        setSelfLink()
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS).getNewlyAddedContentForBanList(effectiveDate)
            ).withRel("Ban List New Content")
        )
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS)
                    .getNewlyRemovedContentForBanList(effectiveDate)
            ).withRel("Ban List Removed Content")
        )
        HateoasLinks.setLinks(forbidden)
        HateoasLinks.setLinks(limited)
        HateoasLinks.setLinks(semiLimited)
    }
}