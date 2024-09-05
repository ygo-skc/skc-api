package com.rtomyj.skc.util.enumeration

enum class TrafficResourceType(
  private val trafficResourceType: String
) {
  CARD("card"),
  PRODUCT("product");

  override fun toString(): String = trafficResourceType
}