package com.rtomyj.skc.constant

object DBQueryConstants {
	private const val WHERE_CARD_NUMBER_IS_CARD_ID = " WHERE card_number = :cardId"
	private const val FROM_PRODUCT_CONTENT_TABLE = " FROM product_contents"

	const val GET_CARD_BY_ID =
		"SELECT card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, monster_association" +
				" FROM card_info" +
				WHERE_CARD_NUMBER_IS_CARD_ID

	const val GET_BAN_LIST_BY_STATUS = "SELECT card_name, monster_type, card_color, card_effect, card_number, card_attribute" +
			" FROM ban_list_info" +
			" WHERE ban_status = :status" +
			" AND ban_list_date = :date" +
			" ORDER BY color_id, card_name"

	const val GET_AVAILABLE_PACKS =
		"SELECT product_id, product_locale, product_name, product_release_date, product_content_total, product_type, product_sub_type, product_sub_type" +
				" FROM product_info" +
				" WHERE product_type = :productType"

	const val GET_PRODUCT_RARITY_INFO = "SELECT card_rarity, count(*) FROM product_details" +
			" WHERE product_id = :productId" +
			" GROUP BY card_rarity" +
			" ORDER by card_rarity"

	const val GET_PRODUCT_CONTENT =
		"SELECT product_id, product_locale, product_name, product_release_date, product_content_total, product_type, product_sub_type, product_position, card_rarity, card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, monster_association" +
				FROM_PRODUCT_CONTENT_TABLE +
				" WHERE product_id = :productId" +
				" AND product_locale = :locale" +
				" ORDER BY product_position"

	const val GET_DATABASE_TOTALS = "SELECT * FROM totals"

	const val GET_BAN_LIST_INFO_FOR_CARD = "SELECT ban_list_date, ban_status" +
			" FROM ban_lists" +
			WHERE_CARD_NUMBER_IS_CARD_ID +
			" ORDER BY ban_list_date DESC"

	const val SEARCH_QUERY =
		"SELECT card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense" +
				" FROM search" +
				" WHERE card_number LIKE :cardId" +
				" AND MATCH(card_name) AGAINST(:cardName IN BOOLEAN MODE)" +
				" AND card_attribute REGEXP :cardAttribute" +
				" AND card_color REGEXP :cardColor" +
				" AND IFNULL(monster_type, '') REGEXP :monsterType" +
				" ORDER BY color_id, card_name ASC" +
				" LIMIT :limit"

	const val SEARCH_QUERY_WITH_BAN_INFO =
		"SELECT card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, ban_list_date, ban_status" +
				" FROM search_with_ban_info" +
				" WHERE card_number LIKE :cardId" +
				" AND MATCH(card_name) AGAINST('far*' IN BOOLEAN MODE)" +
				" AND card_attribute REGEXP :cardAttribute" +
				" AND card_color REGEXP :cardColor" +
				" AND IFNULL(monster_type, '') REGEXP :monsterType" +
				" ORDER BY color_id, card_name ASC" +
				" LIMIT :limit"

	const val GET_CARD_BROWSE_RESULTS =
		"SELECT card_number, card_name, card_color, monster_type, card_effect, card_attribute, monster_association" +
				" FROM card_info" +
				" WHERE card_color REGEXP :cardColors AND card_attribute REGEXP :attributes" +
				" AND IFNULL(monster_type, '') REGEXP :monsterTypes" +
				" AND IFNULL(monster_type, '') REGEXP :monsterSubTypes %s ORDER BY card_name"
}