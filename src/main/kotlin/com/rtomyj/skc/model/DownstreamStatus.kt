package com.rtomyj.skc.model

import io.swagger.v3.oas.annotations.media.Schema

data class DownstreamStatus(
  @field:Schema(implementation = String::class,
    description = "Name of downstream service - service used by this API",
    example = "SKC Suggestion Engine")
  val name: String,
  @field:Schema(ref = "serviceVersion")
  val version: String,
  @field:Schema(ref = "serviceStatus")
  val status: String
)