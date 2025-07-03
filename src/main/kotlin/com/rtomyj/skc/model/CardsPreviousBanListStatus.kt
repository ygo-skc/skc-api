package com.rtomyj.skc.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
  implementation = CardsPreviousBanListStatus::class,
  description = "Contains info about a cards' previous ban status (forbidden, limited, semi-limited) that changed in reference to a previous logical ban list.",
)
data class CardsPreviousBanListStatus(
  @field:Schema(
    implementation = Card::class,
    description = "Card details",
  )
  val card: Card,

  @field:Schema(
    implementation = String::class,
    description = "The previous ban status the card had when compared to current ban list.",
  )
  val previousBanStatus: String
)