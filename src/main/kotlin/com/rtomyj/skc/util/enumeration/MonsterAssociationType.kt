package com.rtomyj.skc.util.enumeration

enum class MonsterAssociationType(
    private val assocationType: String
) {
    LEVEL("level"),
    RANK("rank"),
    LINK("link");


    override fun toString(): String = assocationType
}