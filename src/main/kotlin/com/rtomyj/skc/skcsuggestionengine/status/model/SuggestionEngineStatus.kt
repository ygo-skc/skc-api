package com.rtomyj.skc.skcsuggestionengine.status.model

data class SuggestionEngineStatus(
	val version: String,
	val downstream: List<SuggestionEngineDownstreamStatus>
)