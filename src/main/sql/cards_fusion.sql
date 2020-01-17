USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'17412721', (select color_id from card_colors where card_color = 'Fusion'), 'Elder Entity Norden', 'Water',
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

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'20366274', (select color_id from card_colors where card_color = 'Fusion'), 'El Shaddoll Construct', 'Light',
	"1 'Shaddoll' monster + 1 LIGHT monster
Must first be Fusion Summoned. If this card is Special Summoned: You can send 1 'Shaddoll' card from your Deck to the GY. At the start of the Damage Step, if this card battles a Special Summoned monster: Destroy that monster. If this card is sent to the GY: You can target 1 'Shaddoll' Spell/Trap in your GY; add it to your hand."
	, 'Fairy/Fusion/Effect', 2800, 2500
);

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'63519819', (select color_id from card_colors where card_color = 'Fusion'), 'Thousand-Eyes Restrict', 'Dark',
	"'Relinquished' + 'Thousand-Eyes Idol'
Other monsters on the field cannot change their battle positions or attack. Once per turn: You can target 1 monster your opponent controls; equip that target to this card (max. 1). This card's ATK/DEF become equal to that equipped monster's. If this card would be destroyed by battle, destroy that equipped monster instead."
	, 'Spellcaster/Fusion/Effect', 0, 0
);

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'15291624', (select color_id from card_colors where card_color = 'Fusion'), 'Thunder Dragon Colossus', 'Dark',
	"'Thunder Dragon' + 1 Thunder monster
Must be either Fusion Summoned, or Special Summoned during the turn a Thunder monster's effect was activated in the hand, by Tributing 1 Thunder Effect non-Fusion Monster (in which case you do not use 'Polymerization'). Cards cannot be added from the Main Deck to your opponent's hand except by drawing them. If this card would be destroyed by battle or card effect, you can banish 1 Thunder monster from your GY instead."
	, 'Thunder/Fusion/Effect', 2600, 2400
);

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'39512984', (select color_id from card_colors where card_color = 'Fusion'), 'Gem-Knight Master Diamond', 'Earth',
	"3 'Gem-Knight' monsters
Must first be Fusion Summoned. This card gains 100 ATK for each 'Gem-' monster in your Graveyard. Once per turn: You can banish 1 Level 7 or lower 'Gem-Knight' Fusion Monster from your Graveyard; until the End Phase, this card's name becomes that monster's, and replace this effect with that monster's original effects."
	, 'Rock/Fusion/Effect', 2900, 2500
);

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'48063985', (select color_id from card_colors where card_color = 'Fusion'), 'Ritual Beast Ulti-Cannahawk', 'Wind',
	"1 'Ritual Beast Tamer' monster + 1 'Spiritual Beast' monster
Must be Special Summoned (from your Extra Deck) by banishing the above cards you control, and cannot be Special Summoned by other ways. (You do not use 'Polymerization'.) Once per turn: You can target 2 of your banished 'Ritual Beast' cards; return them to the Graveyard, and if you do, add 1 'Ritual Beast' card from your Deck to your hand. During either player's turn: You can return this card you control to the Extra Deck, then target 2 of your banished monsters (1 'Ritual Beast Tamer' monster and 1 'Spiritual Beast' monster); Special Summon them in Defense Position."
	, 'Thunder/Fusion/Effect', 1400, 1600
);