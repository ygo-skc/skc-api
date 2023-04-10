package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.find.CardController
import com.rtomyj.skc.util.HateoasLinks
import com.rtomyj.skc.util.constant.SwaggerConstants
import com.rtomyj.skc.util.enumeration.BanListFormat
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import java.util.function.Consumer

/**
 * Class defines properties a Yugioh card can contain.
 */
@JsonInclude(
	JsonInclude.Include.NON_EMPTY
) // serializes non-null fields - ie returns non-null fields from REST request
@Schema(
	implementation = Card::class,
	description = "Describes attributes of a Yu-Gi-Oh! card.",
)
data class Card(
	@Schema(
		implementation = String::class,
		description = SwaggerConstants.CARD_ID_DESCRIPTION
	)
	val cardID: String,

	@Schema(
		implementation = String::class,
		description = SwaggerConstants.CARD_NAME_DESCRIPTION
	)
	val cardName: String,

	@Schema(
		implementation = String::class,
		description = SwaggerConstants.CARD_COLOR_DESCRIPTION
	)
	val cardColor: String,

	@Schema(
		implementation = String::class,
		description = SwaggerConstants.CARD_ATTRIBUTE_DESCRIPTION
	)
	val cardAttribute: String,

	@Schema(
		implementation = String::class,
		description = SwaggerConstants.CARD_EFFECT_DESCRIPTION
	)
	var cardEffect: String
) : RepresentationModel<Card>(), HateoasLinks {

	companion object {
		@JvmStatic
		@JsonIgnore
		private val MAX_CARD_EFFECT_LENGTH = 120

		@JvmStatic
		@JsonIgnore
		private val CARD_EFFECT_TRIM_TERMINATION = "..."

		@JvmStatic
		private val cardController = CardController::class.java

		@JvmStatic
		fun trimEffect(effect: String): String {
			return if (effect.length > MAX_CARD_EFFECT_LENGTH) effect.substring(
				0,
				MAX_CARD_EFFECT_LENGTH
			) + CARD_EFFECT_TRIM_TERMINATION else effect
		}


		@JvmStatic
		fun trimEffect(card: Card) {
			card.cardEffect = trimEffect(card.cardEffect)
		}


		/**
		 * Modifies a list of cards to trim card effects to save on bandwidth
		 * @param cards A list of Card objects whose effects have to be trimmed.
		 */
		@JvmStatic
		fun trimEffects(cards: List<Card>) {
			cards
				.forEach(Consumer { card: Card -> trimEffect(card) })
		}


		@JvmStatic
		fun trimEffects(banListInstance: BanListInstance) {
			trimEffects(banListInstance.forbidden)
			trimEffects(banListInstance.limited)
			trimEffects(banListInstance.semiLimited)
		}
	}

	@Schema(
		implementation = String::class,
		description = SwaggerConstants.MONSTER_TYPE_DESCRIPTION
	)
	var monsterType: String? = null

	@Schema(
		implementation = MonsterAssociation::class,
		description = SwaggerConstants.MONSTER_ASSOCIATION_DESCRIPTION
	)
	var monsterAssociation: MonsterAssociation? = null

	//	Using Integer object since I only want to serialize non-null values. An int primitive has a default value of 0.
	@Schema(
		implementation = Int::class,
		description = SwaggerConstants.MONSTER_ATK_DESCRIPTION
	)
	var monsterAttack: Int? = null

	@Schema(
		implementation = Int::class,
		description = SwaggerConstants.MONSTER_DEF_DESCRIPTION
	)
	var monsterDefense: Int? = null

	@Schema(
		implementation = MutableList::class,
		description = SwaggerConstants.RESTRICTED_IN_DESCRIPTION
	)
	var restrictedIn: Map<BanListFormat, MutableList<CardBanListStatus>>? = null

	@Schema(
		implementation = MutableList::class,
		description = SwaggerConstants.PRODUCTS_CARD_IS_FOUND_IN_DESCRIPTION
	)
	var foundIn: MutableList<Product>? = null


	override fun setSelfLink() {
		this.add(
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(cardController).getCard(
					cardID, true
				)
			).withSelfRel()
		)
	}

	override fun setLinks() {
		setSelfLink()
	}

}