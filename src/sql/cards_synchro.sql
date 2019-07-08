USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'25862681', (select color_id from card_colors where card_color = 'Synchro'), 'Ancient Fairy Dragon', 'Light',
	"1 Tuner + 1+ non-Tuner monsters
	Once per turn: You can Special Summon 1 Level 4 or lower monster from your hand. You cannot conduct your Battle Phase the turn you activate this effect. Once per turn: You can destroy as many Field Spells on the field as possible, and if you do, gain 1000 LP, then you can add 1 Field Spell from your Deck to your hand."
	, 'Dragon/Synchro/Effect', 2100, 3000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'65536818', (select color_id from card_colors where card_color = 'Synchro'), 'DENGLONG, FIRST OF THE YANG ZING', 'Light',
	"1 Tuner + 1 or more non-Tuner monsters
If this card is Special Summoned: You can add 1 'Yang Zing' card from your Deck to your hand. Once per turn: You can send 1 Wyrm-Type monster from your Deck to the Graveyard; this card's Level becomes the sent monster's. If this face-up card leaves the field: You can Special Summon 1 'Yang Zing' monster from your Deck. You can only Special Summon 'Denglong, First of the Yang Zing(s)' once per turn."
	, 'Wyrm/Synchro/Tuner/Effect', 0, 2800
);