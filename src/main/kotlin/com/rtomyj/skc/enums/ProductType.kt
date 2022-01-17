package com.rtomyj.skc.enums

enum class ProductType(
    private val product: String
) {
    PACK("PACK"),
    DECK("DECK"),
    SET("SET");


    override fun toString(): String = product
}