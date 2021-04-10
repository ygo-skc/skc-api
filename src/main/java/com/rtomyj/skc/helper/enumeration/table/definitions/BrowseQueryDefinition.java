package com.rtomyj.skc.helper.enumeration.table.definitions;

public enum BrowseQueryDefinition
{

    CARD_ID("card_number")
    , CARD_NAME("card_name")
    , CARD_COLOR("card_color")
    , MONSTER_TYPE("monster_type")
    , CARD_EFFECT("card_effect");

    private String columnName;


    private BrowseQueryDefinition(final String columnName)
    {
        this.columnName = columnName;
    }


    @Override
    public String toString() {
        return columnName;
    }

}
