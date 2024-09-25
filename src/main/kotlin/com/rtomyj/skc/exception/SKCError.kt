package com.rtomyj.skc.exception

import io.swagger.v3.oas.annotations.media.Schema

data class SKCError(
  @Schema(
    implementation = String::class,
    description = "Error message")
  val message: String,
  @Schema(
    implementation = String::class,
    description = "Error code")
  val code: String)