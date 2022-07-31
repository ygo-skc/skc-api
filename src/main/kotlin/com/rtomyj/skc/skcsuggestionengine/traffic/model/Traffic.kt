package com.rtomyj.skc.skcsuggestionengine.traffic.model

data class Traffic(
	val ip: String,
	val source: Source,
	val resourceUtilized: ResourceUtilized
)