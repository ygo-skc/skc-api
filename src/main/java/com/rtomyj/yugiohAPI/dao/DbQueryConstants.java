package com.rtomyj.yugiohAPI.dao;

public class DbQueryConstants
{
	public static final String GET_CARD_BY_ID = new StringBuilder()
		.append("SELECT card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense FROM card_info")
		.append(" WHERE card_number = :cardId")
		.toString();

	public static final String GET_BAN_LIST_BY_STATUS = new StringBuilder()
		.append("SELECT card_name, monster_type, card_color, card_effect, card_number FROM ban_list_info")
		.append(" WHERE ban_status = :status AND ban_list_date = :date ORDER BY color_id, card_name")
		.toString();

	public static final String GET_AVAILABLE_PACKS = "SELECT product_id, product_locale, product_name, product_release_date, product_total, product_type FROM product_info where product_type = :productType";

	public static final String GET_product_RARITY_INFO = "SELECT card_rarity, count(*) FROM product_details WHERE product_id = :productId GROUP BY card_rarity ORDER by card_rarity";

	public static final String GET_product_DETAILS = "SELECT product_id, product_locale, product_name, product_release_date, product_total, product_type, product_position, card_rarity, card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, monster_association, monster_links, monster_scales FROM product_contents WHERE product_id = :packId ORDER BY product_position AND product_locale = :locale";
}