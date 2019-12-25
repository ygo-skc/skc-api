USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'45222299', (select color_id from card_colors where card_color = 'Ritual'), 'Evigishki Gustkraken', 'Water',
	"You can Ritual Summon this card with any 'Gishki' Ritual Spell Card. When this card is Ritual Summoned: Look at up to 2 random cards in your opponent's hand, then shuffle 1 of them into the Deck."
	, 'Aqua/Ritual/Effect', 2400, 1000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'11877465', (select color_id from card_colors where card_color = 'Ritual'), 'Evigishki Mind Augus', 'Water',
	"Cannot be Normal Summoned or Set. Must first be Ritual Summoned. You can Ritual Summon this card with any 'Gishki' Ritual Spell Card. When this card is Ritual Summoned: Target up to 5 cards in any Graveyard(s); shuffle those targets to the Deck."
	, 'Aqua/Ritual/Effect', 2500, 2000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'89463537', (select color_id from card_colors where card_color = 'Ritual'), 'Nekroz of Unicore', 'Water',
	"You can Ritual Summon this card with any 'Nekroz' Ritual Spell. Must be Ritual Summoned. You can discard this card, then target 1 'Nekroz' card in your GY, except 'Nekroz of Unicore'; add it to your hand. You can only use this effect of 'Nekroz of Unicore' once per turn. Negate the effects of face-up monsters on the field that were Special Summoned from the Extra Deck."
	, 'Spellcaster/Ritual/Effect', 2300, 1000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'26674724', (select color_id from card_colors where card_color = 'Ritual'), 'Nekroz of Brionac', 'Water',
	"You can Ritual Summon this card with any 'Nekroz' Ritual Spell. Must be Ritual Summoned, without using 'Nekroz of Brionac'. You can only use each of these effects of 'Nekroz of Brionac' once per turn.
● You can discard this card; add 1 'Nekroz' monster from your Deck to your hand, except 'Nekroz of Brionac'.
● You can target up to 2 face-up monsters on the field that were Special Summoned from the Extra Deck; shuffle them into the Deck."
	, 'Warrior/Ritual/Effect', 2300, 1400
);