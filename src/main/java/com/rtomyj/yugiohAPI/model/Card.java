package com.rtomyj.yugiohAPI.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.yugiohAPI.controller.CardController;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

/**
 * Class defines properties a Yugioh card can contain.
 */
@Data
@Builder
@With
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Describes attributes of a YGO card.")
@JsonInclude(Include.NON_EMPTY)	// serializes non null fields - ie returns non null fields from REST request
@Slf4j
public class Card extends RepresentationModel<Card>
{
	/** Name of the card */
	private String cardID;
	private String cardName;
	private String cardColor;
	private String cardAttribute;
	private String monsterAssociation;
	private String monsterType;
	private String cardEffect;
	/**
	 * Using Integer object since I only want to serialize non null values. An int primitive has a default value of 0.
	 */
	private Integer monsterAttack;
	private Integer monsterDefense;

	private String leftScale;
	private String rightScale;

	private List<String> arrows;

	private List<BanList> restrictedIn;

	@JsonIgnore
	private static final int maxCardEffectLength = 120;
	@JsonIgnore
	private static final String cardEffectTrimTermination = "...";

	private static final Class<CardController> cardController = CardController.class;



	public static String trimEffect(final String effect)
	{
		if (effect.length() > maxCardEffectLength)
			return effect.substring(0, maxCardEffectLength) + cardEffectTrimTermination;

		return effect;
	}



	public static void trimEffect(final Card card)
	{
		card.setCardEffect(trimEffect(card.getCardEffect()));
	}



	/**
	 * Modifies a list of cards to trim card effects to save on bandwidth
	 * @param cards A list of Card objects whose effects have to be trimmed.
	 */
	public static void trimEffects(final List<Card> cards)
	{
		cards
			.stream()
			.forEach(Card::trimEffect);
	}



	public static void trimEffects(final BanListInstance banListInstance)
	{
		Card.trimEffects(banListInstance.getForbidden());
		Card.trimEffects(banListInstance.getLimited());
		Card.trimEffects(banListInstance.getSemiLimited());
	}



	private void setLink()
		throws YgoException
	{
		this.add(
			linkTo(methodOn(cardController).getCard(cardID)).withSelfRel()
		);
	}



	public void setLinks()
	{
		this.setLink();
	}



	public static void setLinks(final List<Card> cards)
	{
		cards.forEach(card -> card.setLinks());
	}

}
