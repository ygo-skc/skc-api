USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'7412721', (select color_id from card_colors where card_color = 'Fusion'), 'Elder Entity Norden', 'Water',
	"1 Synchro or Xyz Monster + 1 Synchro or Xyz Monster
	When this card is Special Summoned: You can target 1 Level 4 or lower monster in your Graveyard; Special Summon it, but its effects are negated, also banish it when this card leaves the field."
	, 'Fairy/Fusion/Effect', 2000, 2200
);

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'43387895', (select color_id from card_colors where card_color = 'Fusion'), 'Supreme King Dragon Starving Venomen', 'Dark',
	"2 DARK Pendulum Monsters
Must be either Fusion Summoned, or Special Summoned by Tributing the above cards you control (in which case you do not use 'Polymerization'). Once per turn: You can target 1 other monster on the field or in the GY; until the End Phase, this card's name becomes that monster's original name, and replace this effect with that monster's original effects, also for the rest of this turn, if your monster attacks a Defense Position monster, inflict piercing battle damage to your opponent."
	, 'Dragon/Fusion/Effect', 2800, 2000
);