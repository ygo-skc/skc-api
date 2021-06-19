package com.rtomyj.skc.dao;

import com.rtomyj.skc.helper.enumeration.table.definitions.ProductViewDefinition;
import com.rtomyj.skc.helper.enumeration.table.definitions.ProductsTableDefinition;

public class DBQueryConstants
{

	public static final String GET_CARD_BY_ID = "SELECT card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, monster_association FROM card_info" +
			" WHERE card_number = :cardId";

	public static final String GET_BAN_LIST_BY_STATUS = "SELECT card_name, monster_type, card_color, card_effect, card_number FROM ban_list_info" +
			" WHERE ban_status = :status AND ban_list_date = :date ORDER BY color_id, card_name";

	public static final String GET_AVAILABLE_PACKS = "SELECT product_id, product_locale, product_name, product_release_date, product_content_total, product_type, product_sub_type, product_sub_type FROM product_info where product_type = :productType";

	public static final String GET_AVAILABLE_PRODUCTS_BY_LOCALE = String.format("SELECT %S, %S, %S, %S, %S, %S, %S FROM product_info where product_locale = :locale", ProductsTableDefinition.PRODUCT_ID, ProductsTableDefinition.PRODUCT_LOCALE, ProductsTableDefinition.PRODUCT_NAME, ProductsTableDefinition.PRODUCT_RELEASE_DATE , ProductViewDefinition.PRODUCT_CONTENT_TOTAL, ProductsTableDefinition.PRODUCT_TYPE, ProductsTableDefinition.PRODUCT_SUB_TYPE);

	public static final String GET_PRODUCT_RARITY_INFO = "SELECT card_rarity, count(*) FROM product_details WHERE product_id = :productId GROUP BY card_rarity ORDER by card_rarity";

	public static final String GET_PRODUCT_DETAILS = "SELECT DISTINCT product_id, product_locale, product_name, product_release_date, product_content_total, product_type, product_sub_type FROM product_contents WHERE product_id = :productId AND product_locale = :locale";

	public static final String GET_PRODUCT_CONTENT = "SELECT product_id, product_locale, product_name, product_release_date, product_content_total, product_type, product_sub_type, product_position, card_rarity, card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, monster_association FROM product_contents WHERE product_id = :packId AND product_locale = :locale ORDER BY product_position";

	public static final String GET_DATABASE_TOTALS = "SELECT * FROM totals";

	public static final String GET_PRODUCT_INFO_FOR_CARD = "select product_id, product_locale, product_name, product_release_date, product_type, product_sub_type, product_position, card_rarity from product_contents where card_number = :cardId ORDER BY product_release_date DESC";

	public static final String GET_BAN_LIST_INFO_FOR_CARD = "SELECT ban_list_date, ban_status FROM ban_lists WHERE card_number = :cardId ORDER BY ban_list_date DESC";

	public static final String SEARCH_QUERY = "SELECT card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense" +
			" FROM search" +
			" WHERE card_number LIKE :cardId" +
			" AND card_name LIKE :cardName" +
			" AND card_attribute REGEXP :cardAttribute" +
			" AND card_color REGEXP :cardColor" +
			" AND IFNULL(monster_type, '') REGEXP :monsterType" +
			" ORDER BY color_id, card_name ASC" +
			" LIMIT :limit";

	public static final String SEARCH_QUERY_WITH_BAN_INFO = "SELECT card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, ban_list_date, ban_status" +
			" FROM search_with_ban_info" +
			" WHERE card_number LIKE :cardId" +
			" AND card_name LIKE :cardName" +
			" AND card_attribute REGEXP :cardAttribute" +
			" AND card_color REGEXP :cardColor" +
			" AND IFNULL(monster_type, '') REGEXP :monsterType" +
			" ORDER BY color_id, card_name ASC" +
			" LIMIT :limit";

}