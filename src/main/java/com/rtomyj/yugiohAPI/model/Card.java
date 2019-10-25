package com.rtomyj.yugiohAPI.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Card
{

	public static class Builder
	{
		private String cardName, monsterType, cardColor, cardEffect, cardID, cardAttribute;
		private int monsterAttack, monsterDefense;

		public Builder cardName(String cardName) {this.cardName = cardName; return this;}
		public Builder monsterType(String monsterType) {this.monsterType = monsterType; return this;}
		public Builder cardColor(String cardColor) {this.cardColor = cardColor; return this;}
		public Builder cardEffect(String cardEffect) {this.cardEffect = cardEffect; return this;}
		public Builder cardID(String cardID) {this.cardID = cardID; return this;}
		public Builder cardAttribute(String cardAttribute) {this.cardAttribute = cardAttribute; return this;}
		public Builder monsterAttack(int monsterAttack) {this.monsterAttack = monsterAttack; return this;}
		public Builder monsterDefense(int monsterDefense) {this.monsterDefense = monsterDefense; return this;}

		public Card build()
		{
			Card card = new Card();
			// String
			card.cardName = this.cardName;
			card.monsterType = this.monsterType;
			card.cardColor = this.cardColor;
			card.cardEffect = this.cardEffect;
			card.cardID = this.cardID;
			card.cardAttribute = this.cardAttribute;

			// Ints
			card.monsterAttack = this.monsterAttack;
			card.monsterDefense = this.monsterDefense;
			return card;
		}
	}


	private String cardName, monsterType, cardColor, cardEffect, cardID, cardAttribute;
	private int monsterAttack, monsterDefense;


	public Card() {}


	public static List<Map<String, String>> toHashMap(List<Card> cards)
	{
		List<Map<String, String>> cardHashMapList = new ArrayList<>();
		for (Card card: cards)
		{
			Map<String, String> cardInfo = new HashMap<>();
			cardInfo.put("cardName", card.cardName);
			cardInfo.put("monsterType", card.monsterType);
			cardInfo.put("cardColor", card.cardColor);
			cardInfo.put("cardEffect", card.cardEffect);
			cardInfo.put("cardID", card.cardID);

			cardHashMapList.add(cardInfo);
		}

		return cardHashMapList;
	}


	public static Map<String, String> toHashMap(Card card)
	{
		Map<String, String> cardInfo = new LinkedHashMap<>();
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
