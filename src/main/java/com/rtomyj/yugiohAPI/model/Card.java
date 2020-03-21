package com.rtomyj.yugiohAPI.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Class defines properties a Yugioh card can contain.
 */
@Data
@Builder
@With
@EqualsAndHashCode
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
	 * Modifies a list of cards to trim card effects to save on bandwidth
	 * @param cards A list of Card objects whose effects have to be trimmed.
	 */
	public static void trimEffects(final List<Card> cards)
	{
		for ( Card card: cards )
		{
			if ( card.getCardEffect().length() > 160 )	card.setCardEffect(card.getCardEffect().substring(0, 160) + "...");
		}
	}



	public static void trimEffects(final BanListInstance banListInstance)
	{
		Card.trimEffects(banListInstance.getForbidden());
		Card.trimEffects(banListInstance.getLimited());
		Card.trimEffects(banListInstance.getSemiLimited());
	}
}
