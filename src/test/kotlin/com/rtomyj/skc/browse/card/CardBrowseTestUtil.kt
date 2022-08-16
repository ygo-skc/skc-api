package com.rtomyj.skc.browse.card

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rtomyj.skc.browse.card.model.Card
import com.rtomyj.skc.browse.card.model.CardBrowseCriteria
import com.rtomyj.skc.constant.TestConstants
import org.springframework.core.io.ClassPathResource

class CardBrowseTestUtil {
	companion object {
		val cardBrowseCriteria = CardBrowseCriteria(
			setOf("Effect", "Fusion", "Pendulum-Xyz"),
			setOf("Dark", "Divine", "Earth", "Fire", "Light", "Water", "Wind"),
			setOf("Aqua", "Beast-Warrior", "Divine-Beast", "Psychic", "Winged Beast"),
			setOf("Flip", "Gemini", "Spirit", "Toon", "Tuner", "Union"),
			setOf(4, 6, 12),
			setOf(1, 3, 11),
			setOf(2, 5, 10)
		)

		private val mapper = jacksonObjectMapper()

		val stratos: Card = mapper
			.readValue(ClassPathResource(TestConstants.CARD_INSTANCE_STRATOS).file, Card::class.java)
	}
}