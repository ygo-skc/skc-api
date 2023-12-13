package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel

@Schema(
  implementation = BanListDates::class,
  description = "Start dates of ban lists.",
)
data class BanListDates(
  @Schema(
    implementation = BanListDates::class,
    description = "Array of objects containing valid start dates of all ban lists currently in DB."
  )
  @JsonProperty(value = "banListDates", index = 1)
  val dates: List<BanListDate>
) : RepresentationModel<BanListDates>()