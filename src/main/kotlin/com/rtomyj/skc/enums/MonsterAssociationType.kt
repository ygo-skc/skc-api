package com.rtomyj.skc.enums

enum class MonsterAssociationType(
    private val assocationType: String
) {
    LEVEL("level"),
    RANK("rank"),
    LINK("link");


    override fun toString(): String = assocationType
}