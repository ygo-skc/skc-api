package com.rtomyj.yugiohAPI.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Card {

	public Card(String cardName, String monsterType, String cardColor, String cardEffect)
	{
		this.cardName = cardName;
		this.monsterType = monsterType;
		this.cardColor = cardColor;
		this.cardEffect = cardEffect;
	}

	public Card(String cardName, String monsterType, String cardColor, String cardEffect, String cardID, String cardAttribute, int monsterAttack, int monsterDefense) {
		this(cardName, monsterType, cardColor, cardEffect);

		this.cardID = cardID;
		this.cardAttribute = cardAttribute;
		this.monsterAttack = monsterAttack;
		this.monsterDefense = monsterDefense;
	}

	private String cardName, monsterType, cardColor, cardEffect, cardID, cardAttribute;
	private int monsterAttack, monsterDefense;

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


	public static List<HashMap<String, String>> toHashMap(List<Card> cards)
	{
		List<HashMap<String, String>> cardHashMapList = new ArrayList<>();
		for (Card card: cards)
		{
			HashMap<String, String> cardInfo = new HashMap<>();
			cardInfo.put("cardName", card.cardName);
			cardInfo.put("monsterType", card.monsterType);
			cardInfo.put("cardColor", card.cardColor);
			cardInfo.put("cardEffect", card.cardEffect);

			cardHashMapList.add(cardInfo);
		}

		return cardHashMapList;
	}


	public static LinkedHashMap<String, String> toHashMap(Card card)
	{
		LinkedHashMap<String, String> cardInfo = new LinkedHashMap<>();
		cardInfo.put("cardName", card.cardName);
		cardInfo.put("monsterType", card.monsterType);
		cardInfo.put("cardColor", card.cardColor);
		cardInfo.put("cardEffect", card.cardEffect);

		return cardInfo;
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
