

CREATE VIEW card_info
AS
SELECT
	card_number, card_colors.card_color, card_name, cards.card_attribute, cards.card_effect, cards.monster_type, cards.monster_attack, cards.monster_defense, card_colors.color_id, cards.monster_association
FROM
	cards, card_colors
WHERE
	card_colors.color_id = cards.color_id;

CREATE VIEW ban_list_info
AS
SELECT
	card_name, monster_type, card_color, card_effect, card_attribute, card_info.card_number, ban_status, ban_list_date, color_id
FROM
	card_info, ban_lists
WHERE
	card_info.card_number = ban_lists.card_number;