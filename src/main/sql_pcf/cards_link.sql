USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'05043010', (select color_id from card_colors where card_color = 'Link'), 'Firewall Dragon', 'Light',
	"2+ monsters
	Once while face-up on the field (Quick Effect): You can target monsters on the field and/or GY up to the number of monsters co-linked to this card; return them to the hand. If a monster this card points to is destroyed by battle or sent to the GY: You can Special Summon 1 monster from your hand."
	, 'Cyberse/Link/Effect', 2500, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'39064822', (select color_id from card_colors where card_color = 'Link'), 'Knightmare Goblin', 'Wind',
	"2 monsters with different names
If this card is Link Summoned: You can discard 1 card; if this card was co-linked when this effect was activated, you can draw 1 card, also, during your Main Phase this turn, you can Normal Summon 1 monster from your hand to your zone this card points to, in addition to your Normal Summon/Set. You can only use this effect of 'Knightmare Goblin' once per turn. Neither player can target your co-linked monsters with card effects."
	, 'Fiend/Link/Effect', 1300, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'61665245', (select color_id from card_colors where card_color = 'Link'), 'Summon Sorceress', 'Dark',
	"2+ monsters with the same Type, except Tokens
If this card is Link Summoned: You can Special Summon 1 monster from your hand in Defense Position, to your opponent's zone this card points to. You can target 1 face-up monster this card points to; Special Summon 1 monster from your Deck, with the same Type as that monster, to a zone this card points to, but negate its effects. You can only use this effect of 'Summon Sorceress' once per turn."
	, 'Spellcaster/Link/Effect', 2400, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'22953417', (select color_id from card_colors where card_color = 'Link'), 'Topologic Gumblar Dragon', 'Dark',
	"2+ Effect Monsters
If another monster is Special Summoned to a zone a Link Monster points to, while this monster is on the field: Discard 1 or 2 random cards, then your opponent discards the same number. If this card is Extra Linked and your opponent has at least 1 card in their hand: You can make your opponent discard 1 or 2 cards (whichever is higher), then, if they have no more cards in their hand, inflict 3000 damage to them. You can only use 1 'Topologic Gumblar Dragon' effect per turn, and only once that turn."
	, 'Cyberse/Link/Effect', 3000, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'24094258', (select color_id from card_colors where card_color = 'Link'), 'Heavymetalfoes Electrumite', 'Fire',
	"2 Pendulum Monsters
If this card is Link Summoned: You can add 1 Pendulum Monster from your Deck to your Extra Deck face-up. Once per turn: You can target 1 other face-up card you control; destroy it, then add 1 face-up Pendulum Monster from your Extra Deck to your hand. If a card(s) in your Pendulum Zone leaves the field: Draw 1 card. You can only use this effect of 'Heavymetalfoes Electrumite' once per turn."
	, 'Psychic/Link/Effect', 1800, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'63288573', (select color_id from card_colors where card_color = 'Link'), 'Sky Striker Ace - Kagari', 'Fire',
	"1 non-FIRE 'Sky Striker Ace' monster
If this card is Special Summoned: You can target 1 'Sky Striker' Spell in your GY; add it to your hand. Gains 100 ATK for each Spell in your GY. You can only Special Summon 'Sky Striker Ace - Kagari(s)' once per turn."
	, 'Machine/Link/Effect', 1500, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'26692769', (select color_id from card_colors where card_color = 'Link'), 'The Phantom Knights of Rusty Bardiche', 'Dark',
	"2+ DARK monsters
During your Main Phase: You can send 1 'The Phantom Knights' monster from your Deck to the GY, Set 1 'Phantom Knights' Spell/Trap directly from your Deck in your Spell & Trap Zone. If a DARK Xyz Monster(s) is Special Summoned to a zone(s) this card points to while this card is on the field, except during the Damage Step: You can target 1 card on the field; destroy it. You can only use each effect of 'The Phantom Knights of Rusty Bardiche' once per turn. Cannot be used as Link Material."
	, 'Warrior/Link/Effect', 2100, null
);