package com.rtomyj.skc.model

import io.swagger.v3.oas.annotations.media.Schema

data class DatabaseStats(
  @Schema(implementation = Int::class, description = "Total number of products in the database.", example = "340")
  val productTotal: Int,
  @Schema(implementation = Int::class, description = "Total number of cards in the database.", example = "11490")
  val cardTotal: Int,
  @Schema(implementation = Int::class, description = "Total number of ban lists in the database.", example = "80")
  val banListTotal: Int,
  @Schema(implementation = Int::class, description = "Total number of years spanned/covered by ban lists stored in database.", example = "8")
  val yearsOfBanListCoverage: Int
)