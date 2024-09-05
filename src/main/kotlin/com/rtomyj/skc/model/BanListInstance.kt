package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeName
import com.rtomyj.skc.find.BanListDiffController
import com.rtomyj.skc.find.BannedCardsController
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.media.Schema

@JsonTypeName("banListInstance")
@JsonPropertyOrder(
    "effectiveDate",
    "comparedTo",
    "numForbidden",
    "numLimited",
    "numSemiLimited",
    "numLimitedOne",
    "numLimitedTwo",
    "numLimitedThree",
    "forbidden",
    "limited",
    "semiLimited",
    "limitedOne",
    "limitedTwo",
    "limitedThree",
)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(
    implementation = BanListInstance::class,
    description = "Describes and contains information about a specific ban list.",
)
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
) {


    companion object {
        private val banListController = BannedCardsController::class.java
        private val BAN_LIST_DIFF_CONTROLLER_CLASS = BanListDiffController::class.java
    }

    @Schema(
        implementation = Int::class,
        description = "Total number of cards considered limited 1 in this ban list instance; ie size of limited one list.",
    )
    var numLimitedOne: Int = 0

    @Schema(
        implementation = Int::class,
        description = "Total number of cards considered limited 2 in this ban list instance; ie size of limited two list.",
    )
    var numLimitedTwo: Int = 0

    @Schema(
        implementation = Int::class,
        description = "Total number of cards considered limited 3 in this ban list instance; ie size of limited three list.",
    )
    var numLimitedThree: Int = 0

    @Schema(
        implementation = List::class,
        description = "Pool of cards that can only have one occurrence in a deck. You can only use one card from this list in your deck.",
    )
    var limitedOne: List<Card>? = null

    @Schema(
        implementation = List::class,
        description = "Pool of cards that can only have two occurrence in a deck. You can only use two cards from this list in your deck.",
    )
    var limitedTwo: List<Card>? = null

    @Schema(
        implementation = List::class,
        description = "Pool of cards that can only have three occurrence in a deck. You can only use three cards from this list in your deck.",
    )
    var limitedThree: List<Card>? = null

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
}