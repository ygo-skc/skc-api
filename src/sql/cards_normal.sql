USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'07902349', (select color_id from card_colors where card_color = 'Normal'), 'Left Arm of the Forbidden One', 'Dark',
	"A forbidden left arm sealed by magic. Whosoever breaks this seal will know infinite power."
	, 'Spellcaster/Normal', 200, 300
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'44519536', (select color_id from card_colors where card_color = 'Normal'), 'Left Leg of the Forbidden One', 'Dark',
	"A forbidden left leg sealed by magic. Whosoever breaks this seal will know infinite power."
	, 'Spellcaster/Normal', 200, 300
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'70903634', (select color_id from card_colors where card_color = 'Normal'), 'Right Arm of the Forbidden One', 'Dark',
	"A forbidden right arm sealed by magic. Whosoever breaks this seal will know infinite power."
	, 'Spellcaster/Normal', 200, 300
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'08124921', (select color_id from card_colors where card_color = 'Normal'), 'Right Leg of the Forbidden One', 'Dark',
	"A forbidden right leg sealed by magic. Whosoever breaks this seal will know infinite power."
	, 'Spellcaster/Normal', 200, 300
);