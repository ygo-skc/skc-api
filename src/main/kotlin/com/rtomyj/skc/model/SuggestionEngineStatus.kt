package com.rtomyj.skc.model

data class SuggestionEngineStatus(
  val version: String,
  val downstream: List<SuggestionEngineDownstreamStatus>
)