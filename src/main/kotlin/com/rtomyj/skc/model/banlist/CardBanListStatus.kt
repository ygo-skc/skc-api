package com.rtomyj.skc.model.banlist

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.Open
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.banlist.BanListDiffController
import com.rtomyj.skc.controller.banlist.BannedCardsController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import java.util.*

/**
 * Model containing information about a Ban List.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // serializes non null fields - ie returns non null fields from REST request
@ApiModel(
    description = "Each object instance describes a particular card, a start date of a ban list it was a part of, and the specific status (forbidden, limited, semi-limited).",
    parent = RepresentationModel::class
)
@Open
data class CardBanListStatus(
    /**
     * Start date of ban list.
     */
    @ApiModelProperty(
        value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd"
    )
    val banListDate: Date,

    /**
     * The ID of the card.
     */
    @ApiModelProperty(value = SwaggerConstants.CARD_ID_DESCRIPTION, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    val cardID: String,

    /**
     * Whether card is forbidden, limited, or semi-limited
     */
    @ApiModelProperty(
        value = "The ban status for the card (forbidden, limited, semi-limited).",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val banStatus: String
) : RepresentationModel<CardBanListStatus>(), HateoasLinks {

    companion object {
        private val BANNED_CARDS_CONTROLLER_CLASS = BannedCardsController::class.java
        private val BAN_LIST_DIFF_CONTROLLER_CLASS = BanListDiffController::class.java
    }


    override fun setSelfLink() {
        TODO()
    }


    override fun setLinks() {
        val dateConfig = DateConfig()
        val banListDateStr = dateConfig.dBSimpleDateFormat().format(banListDate)

        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BANNED_CARDS_CONTROLLER_CLASS)
                    .getBannedCards(banListDateStr, false, true)
            )
                .withRel("Ban List Content")
        )
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS)
                    .getNewlyAddedContentForBanList(banListDateStr)
            )
                .withRel("Ban List New Content")
        )
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS)
                    .getNewlyRemovedContentForBanList(banListDateStr)
            )
                .withRel("Ban List Removed Content")
        )
    }
}