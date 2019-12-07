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
	'65536818', (select color_id from card_colors where card_color = 'Synchro'), 'Denglong, First of the Yang Zing', 'Light',
	"1 Tuner + 1 or more non-Tuner monsters
If this card is Special Summoned: You can add 1 'Yang Zing' card from your Deck to your hand. Once per turn: You can send 1 Wyrm-Type monster from your Deck to the Graveyard; this card's Level becomes the sent monster's. If this face-up card leaves the field: You can Special Summon 1 'Yang Zing' monster from your Deck. You can only Special Summon 'Denglong, First of the Yang Zing(s)' once per turn."
	, 'Wyrm/Synchro/Tuner/Effect', 0, 2800
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'70583986', (select color_id from card_colors where card_color = 'Synchro'), 'Dewloren, Tiger King of the Ice Barrier', 'Water',
	"1 Tuner + 1 or more non-Tuner WATER monsters
Once per turn, you can return any number of face-up cards you control to the owner's hand. For each card returned to the owner's hand by this effect, this card gains 500 ATK until the End Phase."
	, 'Beast/Synchro/Effect', 2000, 1400
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'18239909', (select color_id from card_colors where card_color = 'Synchro'), 'Ignister Prominence, the Blasting Dracoslayer', 'Fire',
	"1 Tuner + 1 or more non-Tuner Pendulum Monsters
Once per turn: You can target 1 Pendulum Monster on the field or 1 card in the Pendulum Zone; destroy it, and if you do, shuffle 1 card on the field into the Deck. Once per turn: You can Special Summon 1 'Dracoslayer' monster from your Deck in Defense Position, but it cannot be used as a Synchro Material for a Summon."
	, 'Dragon/Synchro/Effect', 2850, 0
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'74586817', (select color_id from card_colors where card_color = 'Synchro'), 'PSY-Framelord Omega', 'Light',
	"1 Tuner + 1+ non-Tuner monsters
Once per turn, during the Main Phase (Quick Effect): You can banish both this face-up card from the field and 1 random card from your opponent's hand, face-up, until your next Standby Phase. Once per turn, during your opponent's Standby Phase: You can target 1 banished card; return it to the GY. If this card is in your GY: You can target 1 other card in the GY; shuffle both that card and this card from the GY into the Deck."
	, 'Psychic/Synchro/Effect', 2800, 2200
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'52687916', (select color_id from card_colors where card_color = 'Synchro'), 'Trishula, Dragon of the Ice Barrier', 'Water',
	"1 Tuner + 2+ non-Tuner monsters
When this card is Synchro Summoned: You can banish up to 1 card each from your opponent's hand, field, and GY. (The card in the hand is chosen at random.)"
	, 'Dragon/Synchro/Effect', 2700, 2000
);