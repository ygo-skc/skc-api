package com.rtomyj.skc.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "Return object for test call endpoint.")
data class StatusResponse(
	@ApiModelProperty(value = "Current status of API.", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
			val status: String,
	@ApiModelProperty(value = "Current API version.", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
			val version: String
)