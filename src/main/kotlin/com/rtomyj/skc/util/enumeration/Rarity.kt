package com.rtomyj.skc.util.enumeration

enum class Rarity(private val rarityName: String) {
  C("Common"),
  R("Rarity"),
  SR("Super Rare"),
  UR("Ultra Rare"),
  SCRT("Secret Rare");


  override fun toString(): String = rarityName
}