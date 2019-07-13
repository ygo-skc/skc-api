USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'28566710', (select color_id from card_colors where card_color = 'Trap'), 'Last Turn', 'Trap',
	"This card can only be activated during your opponent's turn when your Life Points are 1000 or less. Select 1 monster on your side of the field and send all other cards on the field and in their respective owner's hands to their respective Graveyards. After that, your opponent selects and Special Summons 1 monster from their Deck in face-up Attack Position and attacks your selected monster. (Any Battle Damage from this battle is treated as 0.) The player whose monster remains alone on the field at the End Phase of this turn wins the Duel. Any other case results in a DRAW."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'27174286', (select color_id from card_colors where card_color = 'Trap'), 'Return from the Different Dimension', 'Trap',
	"Pay half your Life Points; Special Summon as many of your banished monsters as possible. During the End Phase, banish all monsters that were Special Summoned by this effect."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'93016201', (select color_id from card_colors where card_color = 'Trap'), 'Royal Oppression', 'Trap',
	"Either player can pay 800 Life Points to negate the Special Summon of a monster(s), and/or an effect that Special Summons a monster(s), and destroy those cards."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'57585212', (select color_id from card_colors where card_color = 'Trap'), 'Self-Destruct Button', 'Trap',
	"You can only activate this card when your Life Points are lower than your opponent's Life Points and the difference is 7000 points or more. Both player's Life Points become 0."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'03280747', (select color_id from card_colors where card_color = 'Trap'), 'Sixth Sense', 'Trap',
	"Declare 2 numbers from 1 to 6, then your opponent rolls a six-sided die, and if the result is one of the numbers you declared, you draw that many cards. Otherwise, send a number of cards from the top of your Deck to the Graveyard equal to the result."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'35316708', (select color_id from card_colors where card_color = 'Trap'), 'Time Seal', 'Trap',
	"Skip the Draw Phase of your opponent's next turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'64697231', (select color_id from card_colors where card_color = 'Trap'), 'Trap Dustshoot', 'Trap',
	"Activate only if your opponent has 4 or more cards in their hand. Look at your opponent's hand, select 1 Monster Card in it, and return that card to its owner's Deck."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'80604091', (select color_id from card_colors where card_color = 'Trap'), 'Ultimate Offering', 'Trap',
	"During your Main Phase or your opponent's Battle Phase: You can pay 500 Life Points; immediately after this effect resolves, Normal Summon/Set 1 monster."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'05851097', (select color_id from card_colors where card_color = 'Trap'), "Vanity's Emptiness", 'Trap',
	"Neither player can Special Summon monsters. If a card is sent from the Deck or the field to your Graveyard: Destroy this card."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'61740673', (select color_id from card_colors where card_color = 'Trap'), 'Imperial Order', 'Trap',
	"Negate all Spell effects on the field. Once per turn, during the Standby Phase, you must pay 700 LP (this is not optional), or this card is destroyed."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'30241314', (select color_id from card_colors where card_color = 'Trap'), 'Macro Cosmos', 'Trap',
	"When this card resolves: You can Special Summon 1 'Helios - The Primordial Sun' from your hand or Deck. While this card is face-up on the field, any card sent to the Graveyard is banished instead."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'32723153', (select color_id from card_colors where card_color = 'Trap'), 'Magical Explosion', 'Trap',
	"Activate only while you have no cards in your hand. Inflict 200 damage to your opponent for each Spell Card in your Graveyard."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'82732705', (select color_id from card_colors where card_color = 'Trap'), 'Skill Drain', 'Trap',
	"Activate by paying 1000 Life Points. The effects of all face-up monsters on the field are negated while those monsters are face-up on the field (but their effects can still be activated)."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'41420027', (select color_id from card_colors where card_color = 'Trap'), 'Solemn Judgment', 'Trap',
	"When a monster(s) would be Summoned, OR a Spell/Trap Card is activated: Pay half your LP; negate the Summon or activation, and if you do, destroy that card."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'84749824', (select color_id from card_colors where card_color = 'Trap'), 'Solemn Warning', 'Trap',
	"When a monster(s) would be Summoned, OR when a Spell/Trap Card, or monster effect, is activated that includes an effect that Special Summons a monster(s): Pay 2000 LP; negate the Summon or activation, and if you do, destroy that card."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'73599290', (select color_id from card_colors where card_color = 'Trap'), 'Soul Drain', 'Trap',
	"Activate by paying 1000 Life Points. Monsters that are banished, as well as monsters in the Graveyard, cannot activate their effects (that start a Chain)."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'35125879', (select color_id from card_colors where card_color = 'Trap'), "True King's Return", 'Trap',
	"f this card is sent from the Spell & Trap Zone to the Graveyard: You can target 1 monster on the field; destroy it. You cannot activate the following effects of 'True King's Return' in the same Chain.
● You can target 1 'True Draco' or 'True King' monster in your Graveyard; Special Summon it in Defense Position, also for the rest of this turn, you cannot Special Summon.
● During your opponent's Main Phase, you can: Immediately after this effect resolves, Tribute Summon 1 'True Draco' or 'True King' monster face-up.
You can only use each effect of 'True King's Return' once per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'17078030', (select color_id from card_colors where card_color = 'Trap'), 'Wall of Revealing Light', 'Trap',
	"Activate by paying any multiple of 1000 Life Points. Monsters your opponent controls cannot attack if their ATK is less than or equal to the amount you paid."
	, null, null, null
);