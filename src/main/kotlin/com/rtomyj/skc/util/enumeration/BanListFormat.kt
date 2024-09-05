package com.rtomyj.skc.util.enumeration

enum class BanListFormat(
  private val format: String
) {
  TCG("TCG"),
  DL("DL"),
  MD("MD");

  override fun toString(): String = format
}