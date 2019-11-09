package com.rtomyj.yugiohAPI.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * Class defines properties a Yugioh card can contain
 */
@Data
@Builder
public class Card
{
	/** Name of the card */
	private String cardName;
	private String monsterType;
	private String cardColor;
	private String cardEffect;
	private String cardID;
	private String cardAttribute;
	private int monsterAttack;
	private int monsterDefense;



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
}
