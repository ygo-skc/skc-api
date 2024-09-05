package com.rtomyj.skc.util.enumeration

enum class MonsterAssociationType(
  private val associationType: String
) {
  LEVEL("level"),
  RANK("rank"),
  LINK("link");


  override fun toString(): String = associationType
}