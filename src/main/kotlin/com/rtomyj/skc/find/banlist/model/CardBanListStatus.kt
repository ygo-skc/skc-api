package com.rtomyj.skc.find.banlist.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.find.banlist.controller.BanListDiffController
import com.rtomyj.skc.find.banlist.controller.BannedCardsController
import com.rtomyj.skc.util.HateoasLinks
import com.rtomyj.skc.util.constant.SwaggerConstants
import com.rtomyj.skc.util.enumeration.BanListFormat
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import java.util.*

/**
 * Model containing information about a Ban List.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // serializes non-null fields - ie returns non-null fields from REST request
@Schema(
    implementation = BanListInstance::class,
    description = "Each object instance describes a particular card, a start date of a ban list it was a part of, and the specific status (forbidden, limited, semi-limited).",
)
data class CardBanListStatus(
    /**
     * Start date of ban list.
     */
    @Schema(
        implementation = Date::class,
        description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
    )
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd"
    )
    val banListDate: Date,

    /**
     * The ID of the card.
     */
    @Schema(
        implementation = String::class,
        description = SwaggerConstants.CARD_ID_DESCRIPTION,
    )
    val cardID: String,

    /**
     * Whether card is forbidden, limited, or semi-limited
     */
    @Schema(
        implementation = String::class,
        description = "The ban status for the card (forbidden, limited, semi-limited).",
    )
    val banStatus: String,
//    @JsonIgnore
    val format: BanListFormat
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
                    .getBannedCards(banListDateStr, false, format.toString(), true)
            )
                .withRel("Ban List Content")
        )
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS)
                    .getNewlyAddedContentForBanList(banListDateStr, format.toString())
            )
                .withRel("Ban List New Content")
        )
        this.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS)
                    .getNewlyRemovedContentForBanList(banListDateStr, format.toString())
            )
                .withRel("Ban List Removed Content")
        )
    }
}