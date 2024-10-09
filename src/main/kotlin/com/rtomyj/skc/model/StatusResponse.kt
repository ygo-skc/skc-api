package com.rtomyj.skc.model

import io.swagger.v3.oas.annotations.media.Schema

data class StatusResponse(
  @Schema(ref = "serviceStatus")
  val status: String,
  @Schema(ref = "serviceVersion")
  val version: String,
  val downstream: List<DownstreamStatus>
)