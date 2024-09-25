package com.rtomyj.skc.util.constant

object SwaggerConstants {
  const val HTTP_200_SWAGGER_MESSAGE = "Request processed successfully."

  // Server Error
  const val RESULT_LIMIT_DESCRIPTION = "Max number of records/results to return."
  const val SAVE_BANDWIDTH_DESCRIPTION =
    "If true, certain data values retrieved from DB will be truncated to save bandwidth on transit."

  // Common Ban List endpoint Swagger descriptions
  const val BAN_LIST_FETCH_ALL_DESCRIPTION =
    "If true, compares desired ban list with the ban list it replaced (previous logical ban list) and returns the new cards added and the cards removed in the ban list transition. This option is equivalent to calling three endpoints - Banned Cards endpoint, Newly Banned endpoint and Removed endpoint."
  const val BAN_LIST_START_DATE_DESCRIPTION =
    "Valid start date of a ban list stored in database. Must conform to yyyy-mm-dd format (use /api/v1/ban/dates to see a valid list of start dates)."
  const val RESTRICTED_IN_DESCRIPTION =
    "Information about the ban lists a card was restricted in. Restrictions are any of the ban list statuses (forbidden, limited, semi-limited)."
  const val PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION =
    "The start date of the previous logical ban list - in reference to the requested ban list - that is being used to compare differences in transitioning ban lists."

  // Common Card endpoint Swagger descriptions
  const val CARD_FETCH_ALL_DESCRIPTION =
    "If true, returns information about a card such as ban lists it was restricted (forbidden, limited or semi-limited) in and products it was included in. Otherwise, only basic info about the card is returned."
  const val CARD_ID_DESCRIPTION = "ID given to mostly all Yu-Gi-Oh! cards by Konami that uniquely identifies it."
  const val CARD_NAME_DESCRIPTION = "The mame of a particular Yu-Gi-Oh! card."
  const val CARD_ATTRIBUTE_DESCRIPTION = "One of the six monster attributes (seven including 'Divine')"
  const val CARD_COLOR_DESCRIPTION =
    "A simple identifier for card type. If the card is synchro, the card color is synchro."
  const val MONSTER_TYPE_DESCRIPTION = "Monster type such as Spellcaster."
  const val MONSTER_ASSOCIATION_DESCRIPTION =
    "Description off monsters associations such as Level, Link Rating, Rank, etc."
  const val MONSTER_ATK_DESCRIPTION = "The attack stat of a monster card."
  const val MONSTER_DEF_DESCRIPTION = "The defence stat of a monster card."
  const val CARD_EFFECT_DESCRIPTION = "The text provided in a Yu-Gi-Oh! card either describing its effect or lore."
  const val PRODUCT_LOCALE_DESCRIPTION =
    "A locale denoting products release region. As of now the only locale available is \"en\" but in the future other locales can be added to support products in other regions like Japan."
  const val PRODUCTS_CARD_IS_FOUND_IN_DESCRIPTION =
    "Product information for all the products the card in question can be found in."
  const val PRODUCT_ID_DESCRIPTION = "Unique identifier given by Konami to all products."
  const val PRODUCT_NAME_DESCRIPTION = "Full product name."
  const val PRODUCT_TYPE_DESCRIPTION = "A string identifier used by API/DB to separate or distinguish products."
  const val PRODUCT_SUB_TYPE_DESCRIPTION =
    "A string identifier used by API/DB to separate or distinguish product types, ie, further distinguish products."
  const val PRODUCT_RELEASE_DATE_DESCRIPTION = "Date product was released in locale specified by locale property."
  const val PRODUCT_TOTAL_DESCRIPTION = "Total number of cards in the product."
  const val PRODUCT_RARITY_STATS_DESCRIPTION = "Statistics on card rarities for the product."
  const val PRODUCT_CONTENT_DESCRIPTION = "Actual content for product."
  const val CARD_POSITION_IN_PRODUCT_DESCRIPTION = "The position of the card within the product."
  const val CARD_RARITIES_FOR_POSITION_DESCRIPTION =
    "Set of possible rarities the card can be found in for the given product and position."

  // Swagger tag names
  const val STATUS_CALL_TAG_NAME = "API Status"
  const val BAN_LIST_TAG_NAME = "Ban List"
  const val TAG_CARD_TAG_NAMED = "Card Info"
  const val TAG_PRODUCT_TAG_NAME = "Product Info"
  const val TAG_STATS_NAME = "Database Statistics"
}