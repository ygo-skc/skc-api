USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'29401950', (select color_id from card_colors where card_color = 'Trap'), 'Bottomless Trap Hole', 'Trap',
	"When your opponent Summons a monster(s) with 1500 or more ATK: Destroy that monster(s) with 1500 or more ATK, and if you do, banish it."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'36468556', (select color_id from card_colors where card_color = 'Trap'), 'Ceasefire', 'Trap',
	"If a face-down Defense Position monster or an Effect Monster is on the field: Change all face-down Defense Position monsters on the field to face-up Defense Position (Flip monsters' effects are not activated at this time), also inflict 500 damage to your opponent for each Effect Monster on the field."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'57728570', (select color_id from card_colors where card_color = 'Trap'), 'Crush Card Virus', 'Trap',
	"Tribute 1 DARK monster with 1000 or less ATK; your opponent takes no damage until the end of the next turn after this card resolves, also, you look at your opponent's hand and all monsters they control, and if you do, destroy the monsters among them with 1500 or more ATK, then your opponent can destroy up to 3 monsters with 1500 or more ATK in their Deck."
	, null, null, null
);


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
	'23002292', (select color_id from card_colors where card_color = 'Trap'), 'Red Reboot', 'Trap',
	"When your opponent activates a Trap Card: Negate the activation, and if you do, Set that card face-down, then they can Set 1 Trap directly from their Deck. For the rest of this turn after this card resolves, your opponent cannot activate Trap Cards. You can activate this card from your hand by paying half your LP."
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
	'83555666', (select color_id from card_colors where card_color = 'Trap'), 'Ring of Destruction', 'Trap',
	"During your opponent's turn: Target 1 face-up monster your opponent controls whose ATK is less than or equal to their LP; destroy that face-up monster, and if you do, take damage equal to its original ATK, then inflict damage to your opponent, equal to the damage you took. You can only activate 1 Ring of Destruction' per turn."
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
	'53582587', (select color_id from card_colors where card_color = 'Trap'), 'Torrential Tribute', 'Trap',
	"When a monster(s) is Summoned: Destroy all monsters on the field."
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
	'29843091', (select color_id from card_colors where card_color = 'Trap'), 'Ojama Trio', 'Trap',
	"Special Summon 3 'Ojama Tokens' (Beast-Type/LIGHT/Level 2/ATK 0/DEF 1000) to your opponent's field in Defense Position. They cannot be Tributed for a Tribute Summon, and each time 1 is destroyed, its controller takes 300 damage."
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
&bull; You can target 1 'True Draco' or 'True King' monster in your Graveyard; Special Summon it in Defense Position, also for the rest of this turn, you cannot Special Summon.
&bull; During your opponent's Main Phase, you can: Immediately after this effect resolves, Tribute Summon 1 'True Draco' or 'True King' monster face-up.
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


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'89208725', (select color_id from card_colors where card_color = 'Trap'), 'Metaverse', 'Trap',
	"Take 1 Field Spell from your Deck, and either activate it or add it to your hand."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'97077563', (select color_id from card_colors where card_color = 'Trap'), 'Call of the Haunted', 'Trap',
	"Activate this card by targeting 1 monster in your GY; Special Summon that target in Attack Position. When this card leaves the field, destroy that monster. When that monster is destroyed, destroy this card."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'94192409', (select color_id from card_colors where card_color = 'Trap'), 'Compulsory Evacuation Device', 'Trap',
	"Target 1 monster on the field; return that target to the hand."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'15800838', (select color_id from card_colors where card_color = 'Trap'), 'Mind Crush', 'Trap',
	"Declare 1 card name; if that card is in your opponent's hand, they must discard all copies of it, otherwise you discard 1 random card."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'70342110', (select color_id from card_colors where card_color = 'Trap'), 'Dimensional Prison', 'Trap',
	"When an opponent's monster declares an attack: Target that attacking monster; banish that target."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'54974237', (select color_id from card_colors where card_color = 'Trap'), 'Eradicator Epidemic Virus', 'Trap',
	"Tribute 1 DARK monster with 2500 or more ATK, and declare 1 type of card (Spell or Trap); look at your opponent's hand, all Spells/Traps they control, and all cards they draw until the end of their 3rd turn after this card's activation, and destroy all cards of that type."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'09059700', (select color_id from card_colors where card_color = 'Trap'), 'Infernity Barrier', 'Trap',
	"When your opponent activates a Spell/Trap Card, or monster effect, while you control a face-up Attack Position 'Infernity' monster and have no cards in your hand: Negate the activation, and if you do, destroy that card."
	, null, null, null
);