package com.rtomyj.skc.util.enumeration

enum class MonsterAssociationExpression(
    private val expression: String
) {
    LEVEL_EXPRESSION("\"level\": \"%s\""),
    RANK_EXPRESSION("\"rank\": \"%s\""),
    LINK_RATING_EXPRESSION("\"linkRating\": \"%s\"");


    override fun toString(): String = expression
}