package com.rtomyj.skc.model

import com.rtomyj.skc.browse.CardBrowseController
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    implementation = CardBrowseResults::class,
    description = "Card information for cards that fit requested browse criteria.",
)
class CardBrowseResults(
    @Schema(
        implementation = List::class,
        description = "Card info of all cards that fit criteria requested by user.",
    )
    val results: List<Card>,

    @Schema(
        implementation = Int::class,
        description = "Total browse results.",
    )
    val numResults: Int = 0
) {

    companion object {
        private val cardBrowseController = CardBrowseController::class.java
    }


    @Schema(
        implementation = CardBrowseCriteria::class,
        description = "Criteria used to fetch these results.",
    )
    var requestedCriteria: CardBrowseCriteria? = null
}