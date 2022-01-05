package com.rtomyj.skc.enums.table.definitions;

public enum BrowseQueryDefinition
{

    CARD_ID("card_number")
    , CARD_NAME("card_name")
    , CARD_COLOR("card_color")
    , MONSTER_TYPE("monster_type")
    , CARD_EFFECT("card_effect")
    , CARD_ATTRIBUTE("card_attribute")
    , MONSTER_ASSOCIATION("monster_association");

    private final String columnName;


    BrowseQueryDefinition(final String columnName)
    {
        this.columnName = columnName;
    }


    @Override
    public String toString() {
        return columnName;
    }

}
