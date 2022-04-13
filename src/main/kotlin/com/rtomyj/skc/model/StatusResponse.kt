package com.rtomyj.skc.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
	implementation = StatusResponse::class,
	description = "Status of the API."
)
data class StatusResponse(
	@Schema(
		implementation = String::class,
		description = "Current status of API.",
	)
	val status: String,

	@Schema(
		implementation = String::class,
		description = "Current API version.",
	)
	val version: String,

	val downstream: List<DownstreamStatus>
)

data class DownstreamStatus(
	val name: String,
	val version: String,
	val status: String
)