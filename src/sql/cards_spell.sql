USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'69243953', (select color_id from card_colors where card_color = 'Spell'), 'Butterfly Dagger - Elma', 'Spell',
	"The equipped monster gains 300 ATK. When this card is destroyed and sent to the Graveyard while equipped: You can return this card to the hand."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'57953380', (select color_id from card_colors where card_color = 'Spell'), 'Card of Safe Return', 'Spell',
	"When a monster is Special Summoned from your Graveyard, you can draw 1 card."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'04031928', (select color_id from card_colors where card_color = 'Spell'), 'Change of Heart', 'Spell',
	"Target 1 monster your opponent controls; take control of it until the End Phase."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'67616300', (select color_id from card_colors where card_color = 'Spell'), 'Chicken Game', 'Spell',
	"The player with the lowest LP takes no damage. Once per turn, during the Main Phase: The turn player can pay 1000 LP, then activate 1 of these effects;
● Draw 1 card.
● Destroy this card.
● Your opponent gains 1000 LP.
Neither player can activate cards or effects in response to this effect's activation."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'60682203', (select color_id from card_colors where card_color = 'Spell'), 'Cold Wave', 'Spell',
	"This card can only be activated at the start of Main Phase 1. Until your next turn, you and your opponent cannot play or Set any Spell or Trap Cards."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'17375316', (select color_id from card_colors where card_color = 'Spell'), 'Confiscation', 'Spell',
	"Pay 1000 Life Points. Look at your opponent's hand, select 1 card in it and discard that card."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'44763025', (select color_id from card_colors where card_color = 'Spell'), 'Delinquent Duo', 'Spell',
	"Pay 1000 LP; your opponent discards 1 random card, and if they have any other cards in hand, discard 1 more card of their choice."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'23557835', (select color_id from card_colors where card_color = 'Spell'), 'Dimension Fusion', 'Spell',
	"Pay 2000 Life Points. Both players Special Summon as many of their removed from play monsters as possible."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'42703248', (select color_id from card_colors where card_color = 'Spell'), 'Giant Trunade', 'Spell',
	"Return all Spell and Trap Cards on the field to the hand."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'79571449', (select color_id from card_colors where card_color = 'Spell'), 'Graceful Charity', 'Spell',
	"Draw 3 cards, then discard 2 cards."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'18144506', (select color_id from card_colors where card_color = 'Spell'), "Harpie's Feather Duster", 'Spell',
	"Destroy all Spell and Trap Cards your opponent controls."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'19613556', (select color_id from card_colors where card_color = 'Spell'), 'Heavy Storm', 'Spell',
	"Destroy all Spell and Trap Cards on the field."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'35059553', (select color_id from card_colors where card_color = 'Spell'), 'Kaiser Colosseum', 'Spell',
	"If there is 1 or more monster(s) on the field of the controller of this card, his/her opponent cannot place a monster on the field if his/her number of monsters would exceed the number of monsters that are on the field of this card's controller. The cards that are already on the field before this card's activation are unaffected by this effect."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'85602018', (select color_id from card_colors where card_color = 'Spell'), 'Last Will', 'Spell',
	"If a monster on your side of the field was sent to your Graveyard this turn, you can Special Summon 1 monster with an ATK of 1500 points or less from your Deck once during this turn. Then shuffle your Deck."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'34906152', (select color_id from card_colors where card_color = 'Spell'), 'Mass Driver', 'Spell',
	"Tribute 1 monster on your side of the field to inflict 400 points of damage to your opponent's Life Points."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'46411259', (select color_id from card_colors where card_color = 'Spell'), 'Metamorphosis', 'Spell',
	"Tribute 1 monster. Special Summon 1 Fusion Monster from your Extra Deck with the same Level as the Tributed monster."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'41482598', (select color_id from card_colors where card_color = 'Spell'), 'Mirage of Nightmare', 'Spell',
	"Once per turn, during your opponent's Standby Phase: Draw cards until you have 4 cards in your hand. If you do, during your next Standby Phase after that: Randomly discard the same number of cards you drew (or your entire hand, if you do not have enough cards)."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'74191942', (select color_id from card_colors where card_color = 'Spell'), 'Painful Choice', 'Spell',
	"Select 5 cards from your Deck and show them to your opponent. Your opponent selects 1 card among them. Add that card to your hand and discard the remaining cards to the Graveyard."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'67169062', (select color_id from card_colors where card_color = 'Spell'), 'Pot of Avarice', 'Spell',
	"Target 5 monsters in your Graveyard; shuffle all 5 into the Deck, then draw 2 cards."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'55144522', (select color_id from card_colors where card_color = 'Spell'), 'Pot of Greed', 'Spell',
	"Draw 2 cards."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'70828912', (select color_id from card_colors where card_color = 'Spell'), 'Premature Burial', 'Spell',
	"Activate this card by paying 800 LP, then target 1 monster in your Graveyard; Special Summon that target in Attack Position and equip it with this card. When this card is destroyed, destroy the equipped monster."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'94220427', (select color_id from card_colors where card_color = 'Spell'), 'Rank-Up-Magic Argent Chaos Force', 'Spell',
	"Target 1 Rank 5 or higher Xyz Monster you control; Special Summon from your Extra Deck, 1 'Number C' or 'CXyz' monster that is 1 Rank higher than that monster you control, by using it as the Xyz Material. (This Special Summon is treated as an Xyz Summon. Xyz Materials attached to it also become Xyz Materials on the Summoned monster.) When a Rank 5 or higher Xyz Monster is Special Summoned to your side of the field while this card is in your Graveyard (except during the Damage Step): You can add this card from your Graveyard to your hand. You can only use this effect of 'Rank-Up-Magic Argent Chaos Force' once per Duel."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'45986603', (select color_id from card_colors where card_color = 'Spell'), 'Snatch Steal', 'Spell',
	"Equip only to a monster your opponent controls. Take control of the equipped monster. During each of your opponent's Standby Phases: They gain 1000 Life Points."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'54447022', (select color_id from card_colors where card_color = 'Spell'), 'Soul Charge', 'Spell',
	"Target any number of monsters in your GY; Special Summon them, and if you do, you lose 1000 LP for each monster Special Summoned by this effect. You cannot conduct your Battle Phase the turn you activate this card. You can only activate 1 'Soul Charge' per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'46448938', (select color_id from card_colors where card_color = 'Spell'), 'Spellbook of Judgment', 'Spell',
	"During the End Phase of the turn this card was activated, add 'Spellbook' Spell Cards from your Deck to your hand, except 'Spellbook of Judgment', up to the number of Spell Cards activated after this card's resolution, then, you can Special Summon from your Deck 1 Spellcaster-Type monster whose Level is less than or equal to the number of cards added to your hand by this effect. You can only activate 1 'Spellbook of Judgment' per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'27770341', (select color_id from card_colors where card_color = 'Spell'), 'Super Rejuvenation', 'Spell',
	"During the End Phase of the turn this card was activated, draw a number of cards equal to the combined number of Dragon-Type monsters you discarded or Tributed from your hand or your side of the field this turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'11110587', (select color_id from card_colors where card_color = 'Spell'), 'That Grass Looks Greener', 'Spell',
	"If you have more cards in your Deck than your opponent does: Send cards from the top of your Deck to the Graveyard so you have the same number of cards in the Deck as your opponent."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'42829885', (select color_id from card_colors where card_color = 'Spell'), 'The Forceful Sentry', 'Spell',
	"Look at your opponent's hand. Select 1 card among them and return it to his/her Deck. The Deck is then shuffled."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'03298689', (select color_id from card_colors where card_color = 'Spell'), "The Phantom Knights' Rank-Up-Magic Launch", 'Spell',
	"During the Main Phase: Target 1 DARK Xyz Monster you control with no Xyz Materials; Special Summon from your Extra Deck, 1 DARK Xyz Monster that is 1 Rank higher than that monster you control, by using it as the Xyz Material, and if you do, attach this card to it as additional Xyz Material. (This Special Summon is treated as an Xyz Summon. Xyz Materials attached to it also become Xyz Materials on the Summoned monster.) During your Main Phase: You can banish this card from your Graveyard, then target 1 DARK Xyz Monster you control; attach 1 'The Phantom Knights' monster from your hand to that monster as Xyz Material."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'48130397', (select color_id from card_colors where card_color = 'Spell'), "Super Polymerization", 'Spell',
	"Discard 1 card; Fusion Summon 1 Fusion Monster from your Extra Deck, using monsters from either field as Fusion Material. Neither player can activate cards or effects in response to this card's activation."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'73628505', (select color_id from card_colors where card_color = 'Spell'), "Terraforming", 'Spell',
	"Add 1 Field Spell from your Deck to your hand."
	, null, null, null
);