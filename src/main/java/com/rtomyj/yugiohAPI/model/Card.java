package com.rtomyj.yugiohAPI.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class defines properties a Yugioh card can contain
 */
public class Card
{
	/** Name of the card */
	private String cardName, monsterType, cardColor, cardEffect, cardID, cardAttribute;
	private int monsterAttack, monsterDefense;



	/**
	 * Default constructor
	 */
	public Card() {}



	/**
	 * Transforms a List of Cards to a List of Maps that contain the props of each Card.
	 * @param cards The List of Cards to transform into Maps
	 * @return The List of Maps where each list item corresponds to a Card
	 */
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



	/**
	 * Transforms a Card object into a Map where each key is a prop and the value is the current prop value of the Card object.
	 * @param card Instance to transform into Map
	 * @return Map where the key is the prop and the value is the props value
	 */
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



	public String getCardName() { return this.cardName; }
	public String getMonsterType() { return this.monsterType; }
	public String getCardColor() { return this.cardColor; }
	public String getCardEffect() { return this.cardEffect; }
	public String getCardID() { return this.cardID; }
	public String getCardAttribute() { return this.cardAttribute; }
	public int getMonsterAttack() {return this.monsterAttack; }
	public int getMonsterDefense() { return this.monsterDefense; }



	/**
	 * Used to create a Card instance
	 */
	public static class Builder {
		/** Name of the card */
		private String cardName;
		private String monsterType, cardColor, cardEffect, cardID, cardAttribute;
		private int monsterAttack, monsterDefense;

		/**
		 * @param cardName The name of the card.
		 * @return This Builder object.
		 */
		public Builder cardName(String cardName) {
			this.cardName = cardName;
			return this;
		}

		/**
		 * @param monsterType The type of monster card.
		 * @return This Builder object.
		 */
		public Builder monsterType(String monsterType) {
			this.monsterType = monsterType;
			return this;
		}

		/**
		 * @param cardColor The color of the card.
		 * @return This Builder object.
		 */
		public Builder cardColor(String cardColor) {
			this.cardColor = cardColor;
			return this;
		}

		/**
		 * @param cardEffect The effect of the card.
		 * @return This Builder object.
		 */
		public Builder cardEffect(String cardEffect) {
			this.cardEffect = cardEffect;
			return this;
		}

		/**
		 * @param cardID The ID of the card (bottom of card).
		 * @return This Builder object.
		 */
		public Builder cardID(String cardID) {
			this.cardID = cardID;
			return this;
		}

		/**
		 * @param cardAttribute The attribute of the card.
		 * @return This Builder object.
		 */
		public Builder cardAttribute(String cardAttribute) {
			this.cardAttribute = cardAttribute;
			return this;
		}

		/**
		 * @param monsterAttack The attack of the card (if it is a monster card).
		 * @return This Builder object.
		 */
		public Builder monsterAttack(int monsterAttack) {
			this.monsterAttack = monsterAttack;
			return this;
		}

		/**
		 * @param monsterDefense The defense of the card (if it is a monster card).
		 * @return This Builder object.
		 */
		public Builder monsterDefense(int monsterDefense) {
			this.monsterDefense = monsterDefense;
			return this;
		}

		/**
		 * Uses this builder instance to create a new Card instance.
		 *
		 * @return New Card instance.
		 */
		public Card build() {
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
}
