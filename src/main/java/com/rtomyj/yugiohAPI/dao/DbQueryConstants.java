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

	public static final String GET_AVAILABLE_PACKS = "SELECT pack_id, pack_name, pack_release_date, pack_total FROM pack_info";

	public static final String GET_PACK_RARITY_INFO = "SELECT card_rarity, count(*) FROM pack_details WHERE pack_id = :packId GROUP BY card_rarity ORDER by card_rarity";

	public static final String GET_PACK_DETAILS = "SELECT pack_id, pack_name, pack_release_date, pack_total, card_pack_position, card_rarity, card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, monster_association, monster_links, monster_scales FROM pack_contents WHERE pack_id = :packId ORDER BY card_pack_position";
}