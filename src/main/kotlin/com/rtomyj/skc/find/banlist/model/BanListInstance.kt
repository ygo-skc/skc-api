package com.rtomyj.skc.banlist.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeName
import com.rtomyj.skc.Open
import com.rtomyj.skc.banlist.controller.BanListDiffController
import com.rtomyj.skc.banlist.controller.BannedCardsController
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.util.HateoasLinks
import com.rtomyj.skc.browse.card.model.Card
import io.swagger.v3.oas.annotations.media.Schema
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
    implementation = BanListInstance::class,
    description = "Describes and contains information about a specific ban list.",
)
@Open
data class BanListInstance(
	@Schema(
        implementation = String::class,
        description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
    )
    val effectiveDate: String,

	@Schema(
        implementation = String::class,
        description = SwaggerConstants.PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION,
    )
    val comparedTo: String,


	@Schema(
        implementation = List::class,
        description = "List of cards forbidden in this ban list instance.",
    )
    val forbidden: List<Card>,

	@Schema(
        implementation = List::class,
        description = "List of cards limited in this ban list instance.",
    )
    val limited: List<Card>,

	@Schema(
        implementation = List::class,
        description = "List of cards semi-limited in this ban list instance.",
    )
    val semiLimited: List<Card>,

	@Schema(
        implementation = Int::class,
        description = "Total number of cards forbidden in this ban list instance; ie size of forbidden list.",
    )
    val numForbidden: Int = forbidden.size,

	@Schema(
        implementation = Int::class,
        description = "Total number of cards limited in this ban list instance; ie size of limited list.",
    )
    val numLimited: Int = limited.size,

	@Schema(
        implementation = Int::class,
        description = "Total number of cards semi-limited in this ban list instance; ie size of semi-limited list.",
    )
    val numSemiLimited: Int = semiLimited.size
) : RepresentationModel<BanListInstance>(), HateoasLinks {


    companion object {
        private val banListController = BannedCardsController::class.java
        private val BAN_LIST_DIFF_CONTROLLER_CLASS = BanListDiffController::class.java
    }


    @Schema(
        implementation = BanListNewContent::class,
        description = "Object containing info of cards that are newly added to this ban list compared to previous logical ban list. Note: this field will be null unless specified otherwise.",
    )
    var newContent: BanListNewContent? = null

    @Schema(
        implementation = BanListRemovedContent::class,
        description = "Object containing info of cards that are removed from this ban list compared to previous logical ban list. Note: this field will be null unless specified otherwise.",
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