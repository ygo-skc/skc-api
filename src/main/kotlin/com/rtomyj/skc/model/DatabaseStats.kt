package com.rtomyj.skc.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
  implementation = DatabaseStats::class, description = "High level stats for data in the database."
)
data class DatabaseStats(
  @Schema(
    implementation = Int::class, description = "Total number of products in the database."
  ) val productTotal: Int, @Schema(
    implementation = Int::class,
    description = "Total number of cards in the database.",
  ) val cardTotal: Int, @Schema(
    implementation = Int::class,
    description = "Total number of ban lists in the database.",
  ) val banListTotal: Int, @Schema(
    implementation = Int::class,
    description = "Total number of years spanned/covered by ban lists stored in database.",
  ) val yearsOfBanListCoverage: Int
)