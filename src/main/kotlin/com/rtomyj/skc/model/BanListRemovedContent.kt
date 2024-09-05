package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.media.Schema

@JsonPropertyOrder("listRequested", "comparedTo", "numRemoved", "removedCards")
@Schema(
  implementation = BanListRemovedContent::class,
  description = "Cards that were removed from a ban list compared to the previous logical ban list.",
)
data class BanListRemovedContent(
  @Schema(
    implementation = String::class,
    description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
  ) val listRequested: String,

  @Schema(
    implementation = String::class,
    description = SwaggerConstants.PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION,
  ) val comparedTo: String,

  @Schema(
    implementation = List::class,
    description = "List containing removed cards and their previous ban status.",
  ) val removedCards: List<CardsPreviousBanListStatus>,

  @Schema(
    implementation = Int::class,
    description = "Total number of cards removed in requested ban list compared to previous ban list.",
  ) val numRemoved: Int = removedCards.size
)