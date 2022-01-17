package com.rtomyj.skc.constant

import com.rtomyj.skc.model.card.Card

object TestConstants {
    // JSON res files
    const val BAN_LIST_INSTANCE_JSON_FILE = "json-mock/BanListInstanceTestResponse.json"
    const val BAN_LIST_NEW_CONTENT = "json-mock/BanListNewContent.json"
    const val BAN_LIST_REMOVED_CONTENT = "json-mock/BanListRemovedContent.json"

    const val CARD_INSTANCE_JSON_FILE = "json-mock/CardInstance-MockResponse.json"

    // E-HERO Stratos card info
    const val STRATOS_ID = "40044918"
    const val STRATOS_NAME = "Elemental HERO Stratos"
    const val STRATOS_TYPE = "Warrior/Effect"
    const val STRATOS_COLOR = "Effect"
    const val STRATOS_ATTRIBUTE = "Wind"
    const val STRATOS_FULL_EFFECT =
        "When this card is Normal or Special Summoned: You can activate 1 of these effects.\n&bull; You can destroy Spell/Trap Cards on the field, up to the number of \"HERO\" monsters you control, except this card.\n&bull; Add 1 \"HERO\" monster from your Deck to your hand."
    const val STRATOS_ATK = 1800
    const val STRATOS_DEF = 300

    // A HERO lives card info
    const val A_HERO_LIVES_ID = "08949584"
    const val A_HERO_LIVES_NAME = "A Hero Lives"
    const val A_HERO_LIVES_COLOR = "Spell"
    const val A_HERO_LIVES_ATTRIBUTE = "Spell"
    const val A_HERO_LIVES_FULL_EFFECT =
        "If you control no face-up monsters: Pay half your LP; Special Summon 1 Level 4 or lower \"Elemental HERO\" monster from your Deck."

    // D-HERO Mali card info
    const val D_MALICIOUS_ID = "09411399"
    const val D_MALICIOUS_NAME = "Destiny HERO - Malicious"
    const val D_MALICIOUS_TYPE = "Warrior/Effect"
    const val D_MALICIOUS_COLOR = "Effect"
    const val D_MALICIOUS_ATTRIBUTE = "Dark"
    const val D_MALICIOUS_FULL_EFFECT =
        "You can banish this card from your GY; Special Summon 1 \"Destiny HERO - Malicious\" from your Deck."
    const val D_MALICIOUS_ATK = 800
    const val D_MALICIOUS_DEF = 800

    const val ID_THAT_CAUSES_FAILURE = "66666666"
    const val BAN_LIST_START_DATE = "2018-12-03"
    const val PREVIOUS_BAN_LIST_START_DATE = "2018-09-17"
}