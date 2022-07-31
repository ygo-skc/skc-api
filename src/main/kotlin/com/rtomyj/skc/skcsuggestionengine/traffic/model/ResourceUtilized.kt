package com.rtomyj.skc.skcsuggestionengine.traffic.model

import com.rtomyj.skc.util.enumeration.TrafficResourceType

data class ResourceUtilized(
	val name: TrafficResourceType,
	val value: String
)
