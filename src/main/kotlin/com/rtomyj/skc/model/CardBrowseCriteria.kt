package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(
  description = "Valid browse criteria and valid values per criteria. May also be used to identify which browse criteria is being used for browse results.",
  implementation = CardBrowseCriteria::class
)
data class CardBrowseCriteria(
  @field:Schema(
    implementation = Set::class,
    description = "Unique set of identifiers for the type of card.",
    example = "normal, effect, fusion"
  ) val cardColors: Set<String>,

  @field:Schema(
    implementation = Set::class,
    description = "Unique set of identifiers of card attributes.",
    example = "Dark, Light, Earth, Wind, Water, Fire"
  ) val attributes: Set<String>,

  @field:Schema(
    implementation = Set::class,
    description = "Unique set of identifiers for monster types.",
    example = "Spellcaster, Wyrm, Warrior, etc"
  ) val monsterTypes: Set<String>,

  @field:Schema(
    implementation = Set::class,
    description = "Unique set of identifiers for monster sub types.",
    example = "Flip, Toon, Gemini, etc",
  ) val monsterSubTypes: Set<String>,

  @field:Schema(
    implementation = Set::class,
    description = "Unique set of levels for monster cards in database.",
    example = "1, 2, 3, 4, 5, 6",
  ) val levels: Set<Int>,

  @field:Schema(
    implementation = Set::class,
    description = "Unique set of ranks for monster cards in database.",
    example = "1, 2, 3, 4, 5, 6",
  ) val ranks: Set<Int>,

  @field:Schema(
    implementation = Set::class,
    description = "Unique set of link rating values for Link monster cards in database.",
    example = "1, 2, 3, 4, 5, 6",
  ) val linkRatings: Set<Int>
)