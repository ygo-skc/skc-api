package com.rtomyj.yugiohAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        }catch (JSONException e)
        {
            System.out.println("Error creating JSON from Card object " + Card.class);
        }
        return card.toString();
    }


    public static String toJSON(ArrayList<Card> cards)
    {

        String cardJSON = "";
        for (Card card: cards)
        {
            if (cardJSON != "") cardJSON += ", \n";
            cardJSON += card.toJSON();
        }

        return cardJSON;
    }
}
