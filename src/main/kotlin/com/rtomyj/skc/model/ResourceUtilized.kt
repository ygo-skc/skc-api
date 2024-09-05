package com.rtomyj.skc.model

import com.rtomyj.skc.util.enumeration.TrafficResourceType

data class ResourceUtilized(
  val name: TrafficResourceType,
  val value: String
)
