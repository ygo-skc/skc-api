package com.rtomyj.yugiohAPI;

import org.json.JSONObject;

public class Card {

    public Card(String cardName, String monsterType, String cardColor, String cardEffect)
    {
        this.cardName = cardName;
        this.monsterType = monsterType;
        this.cardColor = cardColor;
        this.cardEffect = cardEffect;
    }

    private String cardName, monsterType, cardColor, cardEffect;

    public String toJSON()
    {
        JSONObject card = new JSONObject();
        try
        {
            card.put("cardName", cardName);
            card.put("monsterType", monsterType);
            card.put("cardColor", cardColor);
            card.put("cardEffect", cardEffect);
        }catch (Exception e)
        {

        }
        return card.toString();
    }
}
