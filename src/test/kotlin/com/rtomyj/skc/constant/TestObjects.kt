package com.rtomyj.skc.constant

import com.rtomyj.skc.model.Card

object TestObjects {
	val STRATOS_CARD_FULL_TEXT = Card(
		TestConstants.STRATOS_ID,
		TestConstants.STRATOS_NAME,
		TestConstants.STRATOS_COLOR,
		TestConstants.STRATOS_ATTRIBUTE,
		TestConstants.STRATOS_FULL_EFFECT
	)
		.apply {
			this.monsterType = TestConstants.STRATOS_TYPE
			this.monsterAttack = TestConstants.STRATOS_ATK
			this.monsterDefense = TestConstants.STRATOS_DEF
		}

	val A_HERO_LIVES_CARD_FULL_TEXT = Card(
		TestConstants.A_HERO_LIVES_ID,
		TestConstants.A_HERO_LIVES_NAME,
		TestConstants.A_HERO_LIVES_COLOR,
		TestConstants.A_HERO_LIVES_ATTRIBUTE,
		TestConstants.A_HERO_LIVES_FULL_EFFECT
	)

	val D_MALI_CARD_FULL_TEXT = Card(
		TestConstants.D_MALICIOUS_ID,
		TestConstants.D_MALICIOUS_NAME,
		TestConstants.D_MALICIOUS_COLOR,
		TestConstants.D_MALICIOUS_ATTRIBUTE,
		TestConstants.D_MALICIOUS_FULL_EFFECT
	)
		.apply {
			this.monsterType = TestConstants.D_MALICIOUS_TYPE
			this.monsterAttack = TestConstants.D_MALICIOUS_ATK
			this.monsterDefense = TestConstants.D_MALICIOUS_DEF
		}
}