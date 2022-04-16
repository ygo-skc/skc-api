package com.rtomyj.skc.model.banlist

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.banlist.BanListDiffController
import com.rtomyj.skc.controller.banlist.BannedCardsController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import java.util.*

/**
 * Model containing information about a Ban List.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // serializes non null fields - ie returns non null fields from REST request
@Schema(
    implementation = BanListDate::class,
    description = "Information about a ban lists effective date.",
)
data class BanListDate(
    /**
     * Start date of ban list.
     */
    @Schema(
        implementation = Date::class,
        description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
    ) @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd"
    ) val effectiveDate: Date
) : RepresentationModel<BanListDate?>(), HateoasLinks {

    companion object {
        private val BANNED_CARDS_CONTROLLER_CLASS = BannedCardsController::class.java
        private val BAN_LIST_DIFF_CONTROLLER_CLASS = BanListDiffController::class.java
    }


    override fun setSelfLink() {
        TODO()
    }


    override fun setLinks() {
        val dateConfig = DateConfig()
        val banListDateStr = dateConfig.dBSimpleDateFormat().format(effectiveDate)
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