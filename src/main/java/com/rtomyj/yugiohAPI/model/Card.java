package com.rtomyj.yugiohAPI.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class defines properties a Yugioh card can contain.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Describes attributes of a YGO card.")
@JsonInclude(Include.NON_EMPTY)	// serializes non null fields - ie returns non null fields from REST request
public class Card
{
	/** Name of the card */
	private String cardName;
	private String monsterType;
	private String cardColor;
	private String cardEffect;
	private String cardID;
	private String cardAttribute;
	/**
	 * Using Integer object since I only want to serialize non null values. An int primitive has a default value of 0.
	 */
	private Integer monsterAttack;
	private Integer monsterDefense;

	private List<BanList> banListsFoundIn;



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



	/**
	 * Modifies a list of cards to trim card effects to save on bandwidth
	 * @param cards A list of Card objects whose effects have to be trimmed.
	 */
	public static void trimEffects(List<Card> cards)
	{
		for ( Card card: cards )
		{
			if ( card.getCardEffect().length() > 160 )	card.setCardEffect(card.getCardEffect().substring(0, 160) + "...");
		}
	}
}
