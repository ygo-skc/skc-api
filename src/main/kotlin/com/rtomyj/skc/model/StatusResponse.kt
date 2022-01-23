package com.rtomyj.skc.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "Status of the API.")
data class StatusResponse(
	@ApiModelProperty(value = "Current status of API.", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
	val status: String,
	@ApiModelProperty(value = "Current API version.", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
	val version: String,
	val downstream: List<DownstreamStatus>
)

data class DownstreamStatus(
	val name: String,
	val version: String,
	val status: String
)