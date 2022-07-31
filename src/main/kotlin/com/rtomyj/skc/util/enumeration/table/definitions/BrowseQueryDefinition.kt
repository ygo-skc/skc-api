package com.rtomyj.skc.util.enumeration.table.definitions

enum class BrowseQueryDefinition(
    private val columnName: String
) {
    CARD_ID("card_number"),
    CARD_NAME("card_name"),
    CARD_COLOR("card_color"),
    MONSTER_TYPE("monster_type"),
    CARD_EFFECT("card_effect"),
    CARD_ATTRIBUTE("card_attribute"),
    MONSTER_ASSOCIATION("monster_association");


    override fun toString(): String = columnName
}