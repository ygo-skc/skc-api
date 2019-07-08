USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'00581014', (select color_id from card_colors where card_color = 'Xyz'), 'Daigusto Emeral', 'Wind',
	"2 Level 4 monsters
	Once per turn: You can detach 1 Xyz Material from this card, then activate 1 of these effects.

	● Target 3 monsters in your Graveyard; shuffle all 3 into the Deck, then draw 1 card.

	● Target 1 non-Effect Monster in your Graveyard; Special Summon that target."
	, 'Rock/Xyz/Effect', 1800, 800
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'34086406', (select color_id from card_colors where card_color = 'Xyz'), 'Lavalval Chain', 'Fire',
	"2 Level 4 monsters
Once per turn: You can detach 1 Xyz Material from this card to activate 1 of these effects;
● Send 1 card from your Deck to the Graveyard.
● Choose 1 monster from your Deck and place it on top of your Deck."
	, 'Sea Serpent/Xyz/Effect', 1800, 1000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'04423206', (select color_id from card_colors where card_color = 'Xyz'), 'M-X-Saber Invoker', 'Earth',
	"2 Level 3 monsters
Once per turn: You can detach 1 material from this card; Special Summon 1 Level 4 EARTH Warrior or Beast-Warrior monster from your Deck, in Defense Position, but destroy it during the End Phase."
	, 'Warrior/Xyz/Effect', 1600, 500
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'54719828', (select color_id from card_colors where card_color = 'Xyz'), 'Number 16: Shock Master', 'Light',
	"3 Level 4 monsters
Once per turn: You can detach 1 Xyz Material from this card to declare 1 card type (Monster, Spell, or Trap); that type of card (if Spell or Trap) cannot be activated, or (if Monster) cannot activate its effects, until the end of your opponent's next turn."
	, 'Fairy/Xyz/Effect', 2300, 1600
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'10389142', (select color_id from card_colors where card_color = 'Xyz'), 'Number 42: Galaxy Tomahawk', 'Wind',
	"2 Level 7 monsters
Once per turn: You can detach 2 Xyz Materials from this card; Special Summon as many 'Battle Eagle Tokens' (Machine-Type/WIND/Level 6/ATK 2000/DEF 0) as possible, destroy them during the End Phase of this turn, also your opponent takes no further battle damage this turn."
	, 'Machine/Xyz/Effect', 0, 3000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'63504681', (select color_id from card_colors where card_color = 'Xyz'), 'Number 86: Heroic Champion - Rhongomyniad', 'Dark',
	"2 or more (max. 5) Level 4 Warrior-Type monsters
During each of your opponent's End Phases: Detach 1 Xyz Material from this card. This card gains effects based on the number of Xyz Materials attached to it. ● 1 or more: Cannot be destroyed by battle. ● 2 or more: Gains 1500 ATK and DEF. ● 3 or more: Unaffected by other card effects. ● 4 or more: Your opponent cannot Normal or Special Summon monsters. ● 5: Once per turn: You can destroy all cards your opponent controls."
	, 'Warrior/Xyz/Effect', 1500, 1500
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'58820923', (select color_id from card_colors where card_color = 'Xyz'), 'Number 95: Galaxy-Eyes Dark Matter Dragon', 'Dark',
	"3 Level 9 monsters
You can also Xyz Summon this card by using a 'Galaxy-Eyes' Xyz Monster you control as the Xyz Material. (Xyz Materials attached to that monster also become Xyz Materials on this card.) Cannot be used as an Xyz Material for an Xyz Summon. When this card is Xyz Summoned: You can send 3 Dragon-Type monsters with different names from your Deck to the Graveyard; your opponent banishes 3 monsters from their Deck. You can detach 1 Xyz Material from this card; this card can make up to 2 attacks on monsters during each Battle Phase this turn."
	, 'Dragon/Xyz/Effect', 4000, 0
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'18326736', (select color_id from card_colors where card_color = 'Xyz'), 'Tellarknight Ptolemaeus', 'Light',
	"2 or more Level 4 monsters
Once per Chain, during either player's turn: You can detach 3 Xyz Materials from this card; Special Summon from your Extra Deck, 1 monster that is 1 Rank higher than this card, except a 'Number' monster, by using this face-up card you control as the Xyz Material. (This is treated as an Xyz Summon. Xyz Materials attached to this card also become Xyz Materials on the Summoned monster.) You can detach 7 Xyz Materials from this card; skip your opponent's next turn. During each player's End Phase: You can attach 1 'Stellarknight' card from your Extra Deck to this card as a face-up Xyz Material.."
	, 'Warrior/Xyz/Effect', 550, 2600
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'81122844', (select color_id from card_colors where card_color = 'Xyz'), 'Wind-Up Carrier Zenmaity', 'Water',
	"2 Level 3 monsters
Once per turn: You can detach 1 Xyz Material from this card; Special Summon 1 'Wind-Up' monster from your hand or Deck. When a face-up 'Wind-Up' monster on the field is destroyed and sent to your Graveyard (except during the Damage Step): You can detach 1 Xyz Material from this card to target that monster; return that target to the hand."
	, 'Machine/Xyz/Effect', 1500, 1500
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'85115440', (select color_id from card_colors where card_color = 'Xyz'), 'Zoodiac Broadbull', 'Earth',
	"2 Level 4 monsters
Once per turn, you can also Xyz Summon 'Zoodiac Broadbull' by using 1 'Zoodiac' monster you control with a different name as Xyz Material. (If you used an Xyz Monster, any Xyz Materials attached to it also become Xyz Materials on this card.) This card gains ATK and DEF equal to the ATK and DEF of all 'Zoodiac' monsters attached to it as Materials. Once per turn: You can detach 1 Xyz Material from this card; add 1 Beast-Warrior-Type monster, that can be Normal Summoned/Set, from your Deck to your hand."
	, 'Beast-Warrior/Xyz/Effect', 0, 0
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'48905153', (select color_id from card_colors where card_color = 'Xyz'), 'Zoodiac Drident', 'Earth',
	"4 Level 4 monsters
Once per turn, you can also Xyz Summon 'Zoodiac Drident' by using 1 'Zoodiac' monster you control with a different name as Xyz Material. (If you used an Xyz Monster, any Xyz Materials attached to it also become Xyz Materials on this card.) This card gains ATK and DEF equal to the ATK and DEF of all 'Zoodiac' monsters attached to it as Materials. Once per turn, during either player's turn: You can detach 1 Xyz Material from this card, then target 1 face-up card on the field; destroy it."
	, 'Beast-Warrior/Xyz/Effect', 0, 0
);