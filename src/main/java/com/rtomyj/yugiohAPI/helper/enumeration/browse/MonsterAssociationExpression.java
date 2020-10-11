package com.rtomyj.yugiohAPI.helper.enumeration.browse;

public enum MonsterAssociationExpression
{

    LEVEL_EXPRESSION("\"level\": \"%s\"")
    , RANK_EXPRESSION("\"rank\": \"%s\"")
    , LINK_RATING_EXPRESSION("\"linkRating\": \"%s\"");


    private final String expression;

    MonsterAssociationExpression(final String expression)
    {
        this.expression = expression;
    }


    @Override
    public String toString()
    {
        return this.expression;
    }

}
