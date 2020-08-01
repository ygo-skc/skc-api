package com.rtomyj.yugiohAPI.helper.enumeration.table.definitions;

public enum BrowseQueryDefinition
{

    CARD_NAME("card_name")
    , CARD_COLOR("card_color")
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
