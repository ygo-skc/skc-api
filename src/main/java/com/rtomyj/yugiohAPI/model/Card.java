package com.rtomyj.yugiohAPI.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Card {

	public Card(String cardName, String monsterType, String cardColor, String cardEffect, String cardID)
	{
		this.cardName = cardName;
		this.monsterType = monsterType;
		this.cardColor = cardColor;
		this.cardEffect = cardEffect;
		this.cardID = cardID;
	}

	public Card(String cardName, String monsterType, String cardColor, String cardEffect, String cardID, String cardAttribute, int monsterAttack, int monsterDefense) {
		this(cardName, monsterType, cardColor, cardEffect, cardID);

		this.cardAttribute = cardAttribute;
		this.monsterAttack = monsterAttack;
		this.monsterDefense = monsterDefense;
	}

	private String cardName, monsterType, cardColor, cardEffect, cardID, cardAttribute;
	private int monsterAttack, monsterDefense;

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
			cardInfo.put("cardID", card.cardID);

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
		cardInfo.put("cardID", card.cardID);
		cardInfo.put("cardAttribute", card.cardAttribute);
		cardInfo.put("monsterAtk", Integer.toString(card.monsterAttack));
		cardInfo.put("monsterDef", Integer.toString(card.monsterDefense));

		return cardInfo;
	}
}
