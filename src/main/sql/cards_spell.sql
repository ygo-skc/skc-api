USE yugioh_API_DB;


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'87910978', (select color_id from card_colors where card_color = 'Spell'), 'Brain Control', 'Spell',
	"Pay 800 LP, then target 1 face-up monster your opponent controls that can be Normal Summoned/Set; take control of that target until the End Phase."
	, null, null, null
);

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'48976825', (select color_id from card_colors where card_color = 'Spell'), 'Burial from a Different Dimension', 'Spell',
	"Target up to 3 banished monsters; return them to the Graveyard."
	, null, null, null
);


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
	'24224830', (select color_id from card_colors where card_color = 'Spell'), 'Called by the Grave', 'Spell',
	"Target 1 monster in your opponent's GY; banish it, and if you do, until the end of the next turn, its effects are negated, as well as the activated effects and effects on the field of monsters with the same original name."
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
	'94886282', (select color_id from card_colors where card_color = 'Spell'), 'Charge of the Light Brigade', 'Spell',
	"Send the top 3 cards of your Deck to the Graveyard; add 1 Level 4 or lower 'Lightsworn' monster from your Deck to your hand."
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
	'62265044', (select color_id from card_colors where card_color = 'Spell'), 'Dragon Ravine', 'Spell',
	"Once per turn: You can discard 1 card, then activate 1 of these effects;
● Add 1 Level 4 or lower 'Dragunity' monster from your Deck to your hand.
● Send 1 Dragon monster from your Deck to the GY."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'06417578', (select color_id from card_colors where card_color = 'Spell'), 'El Shaddoll Fusion', 'Spell',
	"Fusion Summon 1 'Shaddoll' Fusion Monster from your Extra Deck, using monsters from your hand or your side of the field as Fusion Materials. You can only activate 1 'El Shaddoll Fusion' per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'77565204', (select color_id from card_colors where card_color = 'Spell'), 'Future Fusion', 'Spell',
	"During your 1st Standby Phase after this card's activation: Show 1 Fusion Monster in your Extra Deck and send the Fusion Materials listed on it from your Main Deck to the GY. During your 2nd Standby Phase after this card's activation: Fusion Summon 1 Fusion Monster from your Extra Deck with the same name as the monster you showed, and target it with this card. When this card leaves the field, destroy that target. When that target is destroyed, destroy this card."
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
	'37520316', (select color_id from card_colors where card_color = 'Spell'), 'Mind Control', 'Spell',
	"Target 1 monster your opponent controls; until the End Phase, take control of that target, but it cannot declare an attack or be Tributed."
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
	'53208660', (select color_id from card_colors where card_color = 'Spell'), 'Pendulum Call', 'Spell',
	"Discard 1 card; add 2 'Magician' Pendulum Monsters with different names from your Deck to your hand, also, until the end of your opponent's next turn after this card resolves, 'Magician' cards in your Pendulum Zones cannot be destroyed by card effects. You can only activate 1 'Pendulum Call' per turn. You cannot activate this card if you activated a 'Magician' monster's Pendulum Effect this turn."
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
	'96729612', (select color_id from card_colors where card_color = 'Spell'), 'Preparation of Rites', 'Spell',
	"Add 1 Level 7 or lower Ritual Monster from your Deck to your hand, then you can add 1 Ritual Spell from your GY to your hand."
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
	'31222701', (select color_id from card_colors where card_color = 'Spell'), "Wavering Eyes' Rank-Up-Magic Launch", 'Spell',
	"Destroy as many cards in each player's Pendulum Zones as possible, then apply these effects, in sequence, depending on the number of cards destroyed by this effect.
● 1 or more: Inflict 500 damage to your opponent.
● 2 or more: You can add 1 Pendulum Monster from your Main Deck to your hand.
● 3 or more: You can banish 1 card on the field.
● 4: You can add 1 'Wavering Eyes' from your Deck to your hand."
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


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'08949584', (select color_id from card_colors where card_color = 'Spell'), "A Hero Lives", 'Spell',
	"If you control no face-up monsters: Pay half your LP; Special Summon 1 Level 4 or lower 'Elemental HERO' monster from your Deck."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'14087893', (select color_id from card_colors where card_color = 'Spell'), "Book of Moon", 'Spell',
	"Target 1 face-up monster on the field; change that target to face-down Defense Position."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'07394770', (select color_id from card_colors where card_color = 'Spell'), "Brilliant Fusion", 'Spell',
	"When this card is activated: Fusion Summon 1 'Gem-Knight' Fusion Monster from your Extra Deck, using monsters from your Deck as Fusion Material, but change its ATK/DEF to 0. If this card leaves the field, destroy that monster. Once per turn: You can discard 1 Spell; the monster Special Summoned by this card's effect gains ATK/DEF equal to its original ATK/DEF, until the end of your opponent's turn. You can only activate 1 'Brilliant Fusion' per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'72892473', (select color_id from card_colors where card_color = 'Spell'), "Card Destruction", 'Spell',
	"Each player discards their entire hand, then draws the same number of cards they discarded."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'91623717', (select color_id from card_colors where card_color = 'Spell'), "Chain Strike", 'Spell',
	"Activate only as Chain Link 2 or higher; inflict 400 damage to your opponent times the Chain Link number of this card. You cannot activate this card if multiple cards/effects with the same name are in that Chain."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'53129443', (select color_id from card_colors where card_color = 'Spell'), "Dark Hole", 'Spell',
	"Destroy all monsters on the field."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'81674782', (select color_id from card_colors where card_color = 'Spell'), "Dimensional Fissure", 'Spell',
	"Any monster sent to the Graveyard is banished instead."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'15854426', (select color_id from card_colors where card_color = 'Spell'), "Divine Wind of Mist Valley", 'Spell',
	"Once per turn, if a WIND monster you control returns to the hand (except during the Damage Step): You can Special Summon 1 Level 4 or lower WIND monster from your Deck."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'14733538', (select color_id from card_colors where card_color = 'Spell'), "Draco Face-Off", 'Spell',
	"Reveal 1 'Dracoslayer' Pendulum Monster and 1 'Dracoverlord' Pendulum Monster from your Deck, your opponent randomly picks 1 of them for you to place in your Pendulum Zone or Special Summon (your choice), and you add the other card to your Extra Deck face-up. You can only activate 1 'Draco Face-Off' per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'67723438', (select color_id from card_colors where card_color = 'Spell'), "Emergency Teleport", 'Spell',
	"Special Summon 1 Level 3 or lower Psychic-Type monster from your hand or Deck, but banish it during the End Phase of this turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'95308449', (select color_id from card_colors where card_color = 'Spell'), "Final Countdown", 'Spell',
	"Pay 2000 Life Points. After 20 turns have passed (counting the turn you activate this card as the 1st turn), you win the Duel."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'81439173', (select color_id from card_colors where card_color = 'Spell'), "Foolish Burial", 'Spell',
	"Send 1 monster from your Deck to the GY."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'27970830', (select color_id from card_colors where card_color = 'Spell'), "Gateway of the Six", 'Spell',
	"Each time a 'Six Samurai' monster(s) is Normal or Special Summoned, place 2 Bushido Counters on this card. You can remove Bushido Counters from your field to activate these effects.
● 2 Counters: Target 1 'Six Samurai' or 'Shien' Effect Monster; that target gains 500 ATK until the end of this turn.
● 4 Counters: Add 1 'Six Samurai' monster from your Deck or GY to your hand.
● 6 Counters: Target 1 'Shien' Effect Monster in your GY; Special Summon that target."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'75500286', (select color_id from card_colors where card_color = 'Spell'), "Gold Sarcophagus", 'Spell',
	"Banish 1 card from your Deck, face-up. During your second Standby Phase after this card's activation, add that card to your hand."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'66957584', (select color_id from card_colors where card_color = 'Spell'), "Infernity Launcher", 'Spell',
	"Once per turn: You can send 1 'Infernity' monster from your hand to the Graveyard. You can send this card to the Graveyard, then target up to 2 'Infernity' monsters in your Graveyard; Special Summon them. You must have no cards in your hand to activate and to resolve this effect."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'23171610', (select color_id from card_colors where card_color = 'Spell'), "Limiter Removal", 'Spell',
	"Once per turn: You can send 1 'Infernity' monster from your hand to the Graveyard. You can send this card to the Graveyard, then target up to 2 'Infernity' monsters in your Graveyard; Special Summon them. You must have no cards in your hand to activate and to resolve this effect."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'71650854', (select color_id from card_colors where card_color = 'Spell'), "Magical Mid-Breaker Field", 'Spell',
	"Double the ATK of all Machine monsters you currently control, until the end of this turn. During the End Phase of this turn, destroy those monsters."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'43040603', (select color_id from card_colors where card_color = 'Spell'), "Monster Gate", 'Spell',
	"Tribute 1 monster; excavate cards from the top of your Deck until you excavate a monster that can be Normal Summoned/Set. Special Summon it, also send the other excavated cards to the Graveyard."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'83764718', (select color_id from card_colors where card_color = 'Spell'), "Monster Reborn", 'Spell',
	"Target 1 monster in either player's GY; Special Summon it."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'33782437', (select color_id from card_colors where card_color = 'Spell'), "One Day of Peace", 'Spell',
	"Each player draws 1 card, and neither player takes damage until the end of the opponent's next turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'02295440', (select color_id from card_colors where card_color = 'Spell'), "One for One", 'Spell',
	"Send 1 monster from your hand to the Graveyard; Special Summon 1 Level 1 monster from your hand or Deck."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'22842126', (select color_id from card_colors where card_color = 'Spell'), "Pantheism of the Monarchs", 'Spell',
	"Send 1 'Monarch' Spell/Trap Card from your hand to the Graveyard; draw 2 cards. You can banish this card from your Graveyard; reveal 3 'Monarch' Spell/Trap Cards from your Deck, your opponent chooses 1 for you to add to your hand, and you shuffle the rest back into your Deck. You can only use this effect of 'Pantheism of the Monarchs' once per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'12580477', (select color_id from card_colors where card_color = 'Spell'), "Raigeki", 'Spell',
	"Destroy all monsters your opponent controls."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'58577036', (select color_id from card_colors where card_color = 'Spell'), "Reasoning", 'Spell',
	"Your opponent declares a monster Level. Excavate cards from the top of your Deck until you excavate a monster that can be Normal Summoned/Set. If that monster is the same Level as the one declared by your opponent, send all excavated cards to the Graveyard. If not, Special Summon the excavated monster, also send the remaining cards to the Graveyard."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'32807846', (select color_id from card_colors where card_color = 'Spell'), "Reinforcement of the Army", 'Spell',
	"Add 1 Level 4 or lower Warrior monster from your Deck to your hand."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'74845897', (select color_id from card_colors where card_color = 'Spell'), "Rekindling", 'Spell',
	"Special Summon from your Graveyard as many FIRE monsters as possible with 200 DEF, but banish them during the End Phase."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'72405967', (select color_id from card_colors where card_color = 'Spell'), "Royal Tribute", 'Spell',
	"If you control 'Necrovalley': Both players discard any monsters in their hands."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'17639150', (select color_id from card_colors where card_color = 'Spell'), "Saqlifice", 'Spell',
	"Equip only to a 'Qli' monster. It gains 300 ATK and cannot be destroyed by battle. The equipped monster can be treated as 2 Tributes for the Tribute Summon of a 'Qli' monster. If this card is sent from the field to the Graveyard: You can add 1 'Qli' monster from your Deck to your hand."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'73915051', (select color_id from card_colors where card_color = 'Spell'), "Scapegoat", 'Spell',
	"Special Summon 4 'Sheep Tokens' (Beast/EARTH/Level 1/ATK 0/DEF 0) in Defense Position. They cannot be Tributed for a Tribute Summon. You cannot Summon other monsters the turn you activate this card (but you can Normal Set)."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'73468603', (select color_id from card_colors where card_color = 'Spell'), "Set Rotation", 'Spell',
	"Set 2 Field Spells with different names from your Deck on the field (1 on your field, and 1 on your opponent's field). While either of those cards remain Set on the field, neither player can activate or Set other Field Spells."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'54031490', (select color_id from card_colors where card_color = 'Spell'), "Shien's Smoke Signal", 'Spell',
	"Add 1 Level 3 or lower 'Six Samurai' monster from your Deck to your hand."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'52340444', (select color_id from card_colors where card_color = 'Spell'), "Sky Striker Mecha - Hornet Drones", 'Spell',
	"f you control no monsters in your Main Monster Zones: Special Summon 1 'Sky Striker Ace Token' (Warrior/DARK/Level 1/ATK 0/DEF 0) in Defense Position, which cannot be Tributed, and if you have 3 or more Spells in your GY when this effect resolves, the Token's ATK/DEF become 1500 instead."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'71344451', (select color_id from card_colors where card_color = 'Spell'), "Slash Draw
", 'Spell',
	"Discard 1 card; send cards from the top of your Deck to the GY equal to the number of cards your opponent controls, then draw 1 card, and show it. Then, if it was 'Slash Draw', send it to the GY, and if you do, destroy as many cards on the field as possible, then, inflict 2000 damage to your opponent for each card destroyed and sent to the GY by this effect. If the card you drew was not 'Slash Draw', shuffle cards from the GY into your Deck, equal to the number of cards sent from your Deck to the GY by this effect. You can only activate 1 'Slash Draw' per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'54631665', (select color_id from card_colors where card_color = 'Spell'), "SPYRAL Resort", 'Spell',
	"Your opponent cannot target other 'SPYRAL' cards you control with card effects. Once per turn: You can add 1 'SPYRAL' monster from your Deck to your hand. Once per turn, during your End Phase, shuffle 1 monster from your Graveyard into the Deck or destroy this card."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'45305419', (select color_id from card_colors where card_color = 'Spell'), "Symbol of Heritage", 'Spell',
	"Activate only while there are 3 Monster Cards with the same name in your Graveyard. Select 1 of those monsters, Special Summon it, and equip it with this card. When this card is destroyed, destroy the equipped monster."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'70368879', (select color_id from card_colors where card_color = 'Spell'), "Upstart Goblin", 'Spell',
	"Draw 1 card, then your opponent gains 1000 Life Points."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'52155219', (select color_id from card_colors where card_color = 'Spell'), "Salamangreat Circle", 'Spell',
	"Activate 1 of these effects:
● Add 1 “Salamangreat” monster from your Deck to your hand.
●Target 1 'Salamangreat' Link Monster you control that was Link Summoned using a monster with its same name as material; that Link Monster is unaffected by monster effects this turn, except its own.
You can only activate 1 “Salamangreat Circle” per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'35371948', (select color_id from card_colors where card_color = 'Spell'), "Trickstar Light Stage", 'Spell',
	"When this card is activated: You can add 1 'Trickstar' monster from your Deck to your hand. Once per turn: You can target 1 Set card in your opponent's Spell & Trap Zone; while this card is in the Field Zone, that Set card cannot be activated until the End Phase, and your opponent must activate it during the End Phase or else send it to the GY. Each time a 'Trickstar' monster you control inflicts battle or effect damage to your opponent, inflict 200 damage to them."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'98338152', (select color_id from card_colors where card_color = 'Spell'), "Sky Striker Mecha - Widow Anchor", 'Spell',
	"If you control no monsters in your Main Monster Zones: Target 1 Effect Monster on the field; negate that face-up monster's effects until the end of this turn, then, if you have 3 or more Spells in your GY, you can take control of that monster until the End Phase."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'24010609', (select color_id from card_colors where card_color = 'Spell'), "Sky Striker Mecha Modules - Multirole", 'Spell',
	"Once per turn: You can target 1 other card you control; send that card to the GY, also your opponent cannot activate cards or effects in response to your Spell Card activations for the rest of this turn (even if this cards leaves the field). Once per turn, during the End Phase: You can Set 'Sky Striker' Spells with different names from your GY, up to the number of 'Sky Striker' Spell Cards you activated this turn while this card was face-up on the field, but banish them when they leave the field."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'99330325', (select color_id from card_colors where card_color = 'Spell'), "Interrupted Kaiju Slumber", 'Spell',
	"Destroy as many monsters on the field as possible, then Special Summon in Attack Position, 2 'Kaiju' monsters with different names from your Deck (1 on each side), but they cannot change their battle positions, and must attack, if able. During your Main Phase, except the turn this card was sent to the Graveyard: You can banish this card from your Graveyard; add 1 'Kaiju' monster from your Deck to your hand. You can only activate 1 'Interrupted Kaiju Slumber' per turn."
	, null, null, null
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'79844764', (select color_id from card_colors where card_color = 'Spell'), "The Monarchs Stormforth
", 'Spell',
	"Once during this turn, if you would Tribute a monster for a Tribute Summon, you can Tribute 1 monster your opponent controls even though you do not control it. You can only activate 1 'The Monarchs Stormforth' per turn. During the turn you activate this card, you cannot Special Summon monsters from the Extra Deck."
	, null, null, null
);