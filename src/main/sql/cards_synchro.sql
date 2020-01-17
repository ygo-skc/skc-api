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
	'50321796', (select color_id from card_colors where card_color = 'Synchro'), 'Brionac, Dragon of the Ice Barrier', 'Water',
	"1 Tuner + 1 or more non-Tuner monsters
You can discard any number of cards to the Graveyard, then target the same number of cards your opponent controls; return those cards to the hand. You can only use this effect of 'Brionac, Dragon of the Ice Barrier' once per turn."
	, 'Sea-Serpent/Synchro/Effect', 2300, 1400
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
	'94677445', (select color_id from card_colors where card_color = 'Synchro'), 'Ib the World Chalice Justiciar', 'Water',
	"1 Tuner + 1+ non-Tuner monsters
For this card’s Synchro Summon, you can treat 1 'World Chalice' Normal Monster you control as a Tuner. You can only use each of the following effects of 'Ib the World Chalice Justiciar' once per turn.
&bull; If this card is Synchro Summoned: You can add 1 'World Legacy' card from your Deck to your hand.
&bull; If this Synchro Summoned card is sent from the field to the GY: You can Special Summon 1 'World Chalice' monster from your Deck or GY, except 'Ib the World Chalice Justiciar'."
	, 'Spellcaster/Synchro/Tuner/Effect', 1800, 2100
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
	'63101919', (select color_id from card_colors where card_color = 'Synchro'), 'Tempest Magician
', 'Dark',
	"1 Tuner + 1 or more non-Tuner Spellcaster-Type monsters
When this card is Synchro Summoned, place 1 Spell Counter on it. Once per turn, you can discard any number of cards to place 1 Spell Counter on a monster(s) you control for each card you discarded. You can remove all Spell Counters on the field to inflict 500 damage to your opponent for each removed Spell Counter."
	, 'Spellcaster/Synchro/Effect', 2200, 1400
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


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'90953320', (select color_id from card_colors where card_color = 'Synchro'), 'T.G. Hyper Librarian', 'Dark',
	"1 Tuner + 1 or more non-Tuner monsters
If a monster is Synchro Summoned: Draw 1 card. This card must be face-up on the field to activate and to resolve this effect."
	, 'Spellcaster/Synchro/Effect', 2400, 1800
);