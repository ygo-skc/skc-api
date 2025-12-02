package com.rtomyj.skc.util.enumeration

enum class TrafficResourceType(
  private val trafficResourceType: String
) {
  CARD("CARD"),
  PRODUCT("PRODUCT");

  override fun toString(): String = trafficResourceType
}