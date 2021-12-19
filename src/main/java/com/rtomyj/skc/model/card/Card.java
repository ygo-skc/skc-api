package com.rtomyj.skc.model.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.controller.card.CardController;
import com.rtomyj.skc.enums.LinkArrow;
import com.rtomyj.skc.model.HateoasLinks;
import com.rtomyj.skc.model.banlist.BanListInstance;
import com.rtomyj.skc.model.banlist.CardBanListStatus;
import com.rtomyj.skc.model.product.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class defines properties a Yugioh card can contain.
 */
@Data
@Builder
@With
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)	// serializes non null fields - ie returns non null fields from REST request
@Slf4j
@ApiModel(
		description = "Describes attributes of a Yu-Gi-Oh! card."
		, parent = RepresentationModel.class
)
public class Card extends RepresentationModel<Card> implements HateoasLinks {

	@ApiModelProperty(value = SwaggerConstants.CARD_ID_DESCRIPTION)
	private String cardID;

	@ApiModelProperty(value = SwaggerConstants.CARD_NAME_DESCRIPTION)
	private String cardName;

	@ApiModelProperty(value = SwaggerConstants.CARD_COLOR_DESCRIPTION)
	private String cardColor;

	@ApiModelProperty(value = SwaggerConstants.CARD_ATTRIBUTE_DESCRIPTION)
	private String cardAttribute;

	@ApiModelProperty(value = SwaggerConstants.MONSTER_TYPE_DESCRIPTION)
	private String monsterType;

	@ApiModelProperty(value = SwaggerConstants.MONSTER_ASSOCIATION_DESCRIPTION)
	private MonsterAssociation monsterAssociation;

//	Using Integer object since I only want to serialize non null values. An int primitive has a default value of 0.
	@ApiModelProperty(value = SwaggerConstants.MONSTER_ATK_DESCRIPTION)
	private Integer monsterAttack;

	@ApiModelProperty(value = SwaggerConstants.MONSTER_DEF_DESCRIPTION)
	private Integer monsterDefense;

	@ApiModelProperty(value = SwaggerConstants.CARD_EFFECT_DESCRIPTION)
	private String cardEffect;

	@ApiModelProperty(value = SwaggerConstants.RESTRICTED_IN_DESCRIPTION)
	private List<CardBanListStatus> restrictedIn;

	@ApiModelProperty(value = SwaggerConstants.PRODUCTS_CARD_IS_FOUND_IN_DESCRIPTION)
	private List<Product> foundIn;

	@JsonIgnore
	private static final int MAX_CARD_EFFECT_LENGTH = 120;

	@JsonIgnore
	private static final String CARD_EFFECT_TRIM_TERMINATION = "...";

	private static final Class<CardController> cardController = CardController.class;


	public static String trimEffect(final String effect) {
		if (effect.length() > MAX_CARD_EFFECT_LENGTH)
			return effect.substring(0, MAX_CARD_EFFECT_LENGTH) + CARD_EFFECT_TRIM_TERMINATION;

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
	public static void trimEffects(final List<Card> cards) {
		cards
			.forEach(Card::trimEffect);
	}


	public static void trimEffects(final BanListInstance banListInstance) {
		Card.trimEffects(banListInstance.getForbidden());
		Card.trimEffects(banListInstance.getLimited());
		Card.trimEffects(banListInstance.getSemiLimited());
	}


	@Override
	public void setSelfLink() {
		this.add(
			linkTo(methodOn(cardController).getCard(cardID, true)).withSelfRel()
		);
	}


	@Override
	public void setLinks() {
		this.setSelfLink();

		if (restrictedIn != null)
			HateoasLinks.setLinks(restrictedIn);
	}


	/**
	 * Takes monster link rating retrieved from DB (constants denoting position of arrow, eg: T-L (top left), T-R (top right)... etc) and converts them to emojis.
	 */
	public void transformMonsterLinkRating() {
		if (this.getMonsterAssociation() != null
				&& this.getMonsterAssociation().getLinkArrows() != null
				&& !this.getMonsterAssociation().getLinkArrows().isEmpty()) {
			this.getMonsterAssociation().setLinkArrows(
					this
							.getMonsterAssociation()
							.getLinkArrows()
							.stream()
							.map(dbArrowString -> LinkArrow.transformDBStringToEnum(dbArrowString).toString())
							.toList()
			);
		}
	}


	/**
	 * Calls {@link #transformMonsterLinkRating()} on a list of Cards
	 * @param cards list of cards whose link rating should be transformed
	 */
	public static void transformMonsterLinkRating(final List<Card> cards) {
		cards
				.forEach(Card::transformMonsterLinkRating);
	}
}
