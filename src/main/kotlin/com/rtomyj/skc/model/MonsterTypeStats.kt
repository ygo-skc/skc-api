package com.rtomyj.skc.model

import com.rtomyj.skc.stats.StatsController
import com.rtomyj.skc.util.HateoasLinks
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@Schema(
  implementation = MonsterTypeStats::class,
  description = "Statistics for monster types stored in Database."
)
data class MonsterTypeStats(
  @Schema(
    implementation = String::class,
    description = "The scope or filter used when retrieving monster type stats.",
  )
  val scope: String,
  @Schema(
    implementation = MutableMap::class,
    description = "Monster types and the total number of cards currently in Database that have the type for given scope.",
  )
  val monsterTypes: MutableMap<String, Int>
) : RepresentationModel<MonsterTypeStats>(), HateoasLinks {

  override fun setSelfLink() {
    this.add(
      WebMvcLinkBuilder
          .linkTo(
            WebMvcLinkBuilder
                .methodOn(statsControllerClass)
                .monsterTypesForgivenCardColor(
                  scope
                )
          )
          .withSelfRel()
    )
  }


  override fun setLinks() {
    setSelfLink()
  }


  companion object {
    private val statsControllerClass = StatsController::class.java
  }
}