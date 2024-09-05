package com.rtomyj.skc.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
  implementation = MonsterTypeStats::class, description = "Statistics for monster types stored in Database."
)
data class MonsterTypeStats(
  @Schema(
    implementation = String::class,
    description = "The scope or filter used when retrieving monster type stats.",
  ) val scope: String, @Schema(
    implementation = MutableMap::class,
    description = "Monster types and the total number of cards currently in Database that have the type for given scope.",
  ) val monsterTypes: MutableMap<String, Int>
)