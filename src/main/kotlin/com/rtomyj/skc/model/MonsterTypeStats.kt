package com.rtomyj.skc.model

import io.swagger.v3.oas.annotations.media.Schema

data class MonsterTypeStats(
  @field:Schema(implementation = String::class, description = "The scope or filter used when retrieving monster type stats.", example = "fusion")
  val scope: String,
  @field:Schema(
    implementation = MutableMap::class,
    description = "Monster types and the total number of cards currently in Database that have the type for given scope.",
  )
  val monsterTypes: MutableMap<String, Int>
)