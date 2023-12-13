package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel

@JsonPropertyOrder("listRequested", "comparedTo", "newCards")
@Schema(
  implementation = BanListNewContent::class,
  description = "Cards added to requested ban list that were not in the previous ban list and/or cards that have a different ban list status (forbidden, limited, semi-limited) compared to the previous ban list.",
)
data class BanListNewContent(
  @Schema(
    implementation = String::class,
    description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
  )
  val listRequested: String,

  @Schema(
    implementation = String::class,
    description = SwaggerConstants.PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION,
  )
  val comparedTo: String,

  @Schema(
    implementation = List::class,
    description = "List containing newly forbidden cards and their previous ban status.",
  )
  val newForbidden: List<CardsPreviousBanListStatus>,

  @Schema(
    implementation = List::class,
    description = "List containing newly limited cards and their previous ban status.",
  )
  val newLimited: List<CardsPreviousBanListStatus>,

  @Schema(
    implementation = List::class,
    description = "List containing newly semi-limited cards and their previous ban status.",
  )
  val newSemiLimited: List<CardsPreviousBanListStatus>,

  @Schema(
    implementation = List::class,
    description = "List containing cards that are newly added to the Limited 1 list.",
  )
  val newLimitedOne: List<CardsPreviousBanListStatus>,

  @Schema(
    implementation = List::class,
    description = "List containing cards that are newly added to the Limited 2 list.",
  )
  val newLimitedTwo: List<CardsPreviousBanListStatus>,

  @Schema(
    implementation = List::class,
    description = "List containing cards that are newly added to the Limited 3 list.",
  )
  val newLimitedThree: List<CardsPreviousBanListStatus>,

  @Schema(
    implementation = Int::class,
    description = "Total new forbidden cards added to a ban list when compared to a previous logical ban list.",
  )
  val numNewForbidden: Int = newForbidden.size,

  @Schema(
    implementation = Int::class,
    description = "Total new limited cards added to a list when compared to a previous logical ban list.",
  )
  val numNewLimited: Int = newLimited.size,

  @Schema(
    implementation = Int::class,
    description = "Total new semi-limited cards added to a ban list when compared to a previous logical ban list.",
  )
  val numNewSemiLimited: Int = newSemiLimited.size,

  @Schema(
    implementation = Int::class,
    description = "Total new cards included in the Limited One list.",
  )
  val numNewLimitedOne: Int = newLimitedOne.size,

  @Schema(
    implementation = Int::class,
    description = "Total new cards included in the Limited Two list.",
  )
  val numNewLimitedTwo: Int = newLimitedTwo.size,

  @Schema(
    implementation = Int::class,
    description = "Total new cards included in the Limited Three list.",
  )
  val numNewLimitedThree: Int = newLimitedThree.size,
) : RepresentationModel<BanListNewContent>() {
  fun setLinks() {
    newForbidden
        .forEach(CardsPreviousBanListStatus::setLinks)
    newLimited
        .forEach(CardsPreviousBanListStatus::setLinks)
    newSemiLimited
        .forEach(CardsPreviousBanListStatus::setLinks)
  }
}