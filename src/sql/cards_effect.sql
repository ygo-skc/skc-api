USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'09929398', (select color_id from card_colors where card_color = 'Effect'), 'Blackwing - Gofu the Vague Shadow', 'Dark',
	"Cannot be Normal Summoned/Set. Must first be Special Summoned (from your hand) while you control no monsters. When this card is Special Summoned from the hand: You can Special Summon 2 'Vague Shadow Tokens' (Winged Beast-Type/DARK/Level 1/ATK 0/DEF 0), but they cannot be Tributed or be used as Synchro Material. You can banish this card and 1 or more face-up non-Tuners you control, then target 1 'Blackwing' Synchro Monster in your Graveyard whose Level equals the total Levels the banished monsters had on the field; Special Summon it, and if you do, it is treated as a Tuner.",
	'Winged-Beast/Tuner/Effect', 0, 0
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'53804307', (select color_id from card_colors where card_color = 'Effect'), 'Blaster, Dragon Ruler of Infernos', 'Fire',
	"If this card is in your hand or Graveyard: You can banish a total of 2 FIRE and/or Dragon-Type monsters from your hand and/or Graveyard, except this card; Special Summon this card. During your opponent's End Phase, if this card was Special Summoned: Return it to the hand. You can discard this card and 1 FIRE monster to the Graveyard, then target 1 card on the field; destroy that target. If this card is banished: You can add 1 FIRE Dragon-Type monster from your Deck to your hand. You can only use 1 'Blaster, Dragon Ruler of Infernos' effect per turn, and only once that turn.",
	'Dragon/Effect', 2800, 1800
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'34124316', (select color_id from card_colors where card_color = 'Effect'), 'Cyber Jar', 'Dark',
	"FLIP: Destroy all monsters on the field, then both players reveal the top 5 cards from their Decks, then Special Summon all revealed Level 4 or lower monsters in face-up Attack Position or face-down Defense Position, also add any remaining cards to their hand. (If either player has less than 5 cards in their Deck, reveal as many as possible.)",
	'Rock/Effect', 900, 900
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'15341821', (select color_id from card_colors where card_color = 'Effect'), 'Dandylion', 'Earth',
	"If this card is sent to the Graveyard: Special Summon 2 'Fluff Tokens' (Plant-Type/WIND/Level 1/ATK 0/DEF 0) in Defense Position. These Tokens cannot be Tributed for a Tribute Summon during the turn they are Special Summoned.",
	'Plant/Effect', 300, 300
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'08903700', (select color_id from card_colors where card_color = 'Effect'), 'Djinn Releaser of Rituals', 'Dark',
	"When you Ritual Summon a monster, you can banish this card from your Graveyard as 1 of the monsters required for the Ritual Summon. If a player Ritual Summons using this card, the other player cannot Special Summon while that Ritual Summoned monster is face-up on the field.",
	'Fiend/Effect', 1200, 2000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'55623480', (select color_id from card_colors where card_color = 'Effect'), 'Fairy Tail - Snow', 'Light',
	"If this card is Normal or Special Summoned: You can target 1 face-up monster your opponent controls; change it to face-down Defense Position. If this card is in your GY (Quick Effect): You can banish 7 other cards from your hand, field, and/or GY; Special Summon this card.",
	'Spellcaster/Effect', 1850, 1000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'78706415', (select color_id from card_colors where card_color = 'Effect'), 'Fiber Jar', 'Earth',
	"FLIP: Each player shuffles all cards from their hand, field, and Graveyard into the Deck, then draws 5 cards.",
	'Plant/Effect', 500, 500
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'93369354', (select color_id from card_colors where card_color = 'Effect'), 'Fishborg Blaster', 'Water',
	"If you control a face-up Level 3 or lower WATER monster: You can discard 1 card; Special Summon this card from your Graveyard. If this card is used as a Synchro Material Monster, all other Synchro Material Monsters must be WATER.",
	'Fish/Tuner/Effect', 100, 200
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'75732622', (select color_id from card_colors where card_color = 'Effect'), 'Grinder Golem', 'Dark',
	"Cannot be Normal Summoned or Set. Must first be Special Summoned (from your hand) to your opponent's side of the field by Special Summoning 2 'Grinder Tokens' (Fiend-Type/DARK/Level 1/ATK 0/DEF 0) in face-up Attack Position on your side of the field. If you Special Summon this monster, you cannot Normal Summon or Set a monster during the same turn.",
	'Fiend/Effect', 3000, 300
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'57421866', (select color_id from card_colors where card_color = 'Effect'), 'Level Eater', 'Dark',
	"If this card is in your Graveyard: You can target 1 Level 5 or higher monster you control; reduce its Level by 1, and if you do, Special Summon this card. This face-up card on the field cannot be Tributed, except for a Tribute Summon.",
	'Insect/Effect', 600, 0
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'34206604', (select color_id from card_colors where card_color = 'Effect'), 'Magical Scientist', 'Dark',
	"Pay 1000 Life Points to Special Summon 1 level 6 or lower Fusion Monster from your Extra Deck in face-up Attack or Defense Position. That Fusion Monster cannot attack your opponent's Life Points directly, and is returned to your Extra Deck at the end of the turn.",
	'Spellcaster/Effect', 300, 300
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'21593977', (select color_id from card_colors where card_color = 'Effect'), 'Makyura the Destructor', 'Dark',
	"During the turn this card was sent to the Graveyard, you can activate Trap Cards from your hand.",
	'Warrior/Effect', 1600, 1200
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'21377582', (select color_id from card_colors where card_color = 'Effect'), 'Master Peace, the True Dracoslaying King', 'Light',
	"To Tribute Summon this card face-up, you can Tribute Continuous Spell/Trap Card(s) you control, as well as monsters. Unaffected by the effects of cards with the same card type (Monster, Spell, and/or Trap) as the original card type of the cards Tributed for its Tribute Summon. Once per turn, during either player's turn, if you control this Tribute Summoned monster: You can banish 1 Continuous Spell/Trap Card from your Graveyard, then target 1 other card on the field; destroy it.",
	'Wyrm/Effect', 2950, 2950
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'23434538', (select color_id from card_colors where card_color = 'Effect'), "Maxx 'C'", 'Earth',
	"During either player's turn: You can send this card from your hand to the Graveyard; this turn, each time your opponent Special Summons a monster(s), immediately draw 1 card. You can only use 1 'Maxx 'C''' per turn.",
	'Insect/Effect', 500, 200
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'96782886', (select color_id from card_colors where card_color = 'Effect'), 'Mind Master', 'Light',
	"You can pay 800 Life Points and Tribute 1 Psychic-Type monster, except 'Mind Master', to Special Summon 1 Level 4 or lower Psychic-Type monster from your Deck in face-up Attack Position.",
	'Psychic/Effect', 100, 200
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'79106360', (select color_id from card_colors where card_color = 'Effect'), 'Morphing Jar #2', 'Earth',
	"FLIP: Shuffle all monsters on the field into the Deck. Then, each player excavates cards from the top of their Deck, until they excavate the same number of monsters they shuffled into their Main Deck. Special Summon all excavated Level 4 or lower monsters in face-down Defense Position, also send the remaining cards to the Graveyard.",
	'Rock/Effect', 800, 700
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'23558733', (select color_id from card_colors where card_color = 'Effect'), 'Phoenixian Cluster Amaryllis', 'Fire',
	"This card cannot be Special Summoned except with its own effect or with 'Phoenixian Seed'. If this card attacks, it is destroyed after damage calculation. If this card you control is destroyed and sent to the Graveyard, inflict 800 damage to your opponent. During your End Phase, you can remove from play 1 Plant-Type monster from your Graveyard to Special Summon this card from your Graveyard in Defense Position.",
	'Rock/Effect', 2300, 0
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'90411554', (select color_id from card_colors where card_color = 'Effect'), 'Redox, Dragon Ruler of Boulders', 'Earth',
	"If this card is in your hand or Graveyard: You can banish a total of 2 EARTH and/or Dragon-Type monsters from your hand and/or Graveyard, except this card; Special Summon this card. During your opponent's End Phase, if this card was Special Summoned: Return it to the hand. You can discard this card and 1 EARTH monster to the Graveyard, then target 1 monster in your Graveyard; Special Summon that target. If this card is banished: You can add 1 EARTH Dragon-Type monster from your Deck to your hand. You can only use 1 'Redox, Dragon Ruler of Boulders' effect per turn, and only once that turn.",
	'Dragon/Effect', 1600, 3000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'05592689', (select color_id from card_colors where card_color = 'Effect'), 'Samsara Lotus', 'Dark',
	"During your End Phase, if you control no Spell or Trap Cards, you can Special Summon this card from your Graveyard in face-up Attack Position. This card's controller takes 1000 damage during each of their Standby Phases.",
	'Plant/Effect', 0, 0
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'20663556', (select color_id from card_colors where card_color = 'Effect'), 'Substitoad', 'Water',
	"You can Tribute 1 monster to Special Summon 1 'Frog' monster from your Deck, except 'Frog the Jam'. 'Frog' monsters, except 'Frog the Jam', cannot be destroyed by battle.",
	'Aqua/Effect', 100, 2000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'89399912', (select color_id from card_colors where card_color = 'Effect'), 'Tempest, Dragon Ruler of Storms', 'Wind',
	"If this card is in your hand or Graveyard: You can banish a total of 2 WIND and/or Dragon-Type monsters from your hand and/or Graveyard, except this card; Special Summon this card. During your opponent's End Phase, if this card was Special Summoned: Return it to the hand. You can discard this card and 1 WIND monster to the Graveyard; add 1 Dragon-Type monster from your Deck to your hand. If this card is banished: You can add 1 WIND Dragon-Type monster from your Deck to your hand. You can only use 1 'Tempest, Dragon Ruler of Storms' effect per turn, and only once that turn.",
	'Dragon/Effect', 2400, 2200
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'88071625', (select color_id from card_colors where card_color = 'Effect'), 'The Tyrant Neptune', 'Water',
	"Cannot be Special Summoned. You can Tribute Summon this card by Tributing 1 monster. This card gains the total original ATK and DEF of the monster(s) Tributed for its Tribute Summon. When this card is Tribute Summoned: Target 1 Effect Monster in the Graveyard that was Tributed for the Tribute Summon; this card's name becomes that target's name, and this card gains that target's effects.",
	'Reptile/Effect', 0, 0
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'26400609', (select color_id from card_colors where card_color = 'Effect'), 'Tidal, Dragon Ruler of Waterfalls', 'Water',
	"If this card is in your hand or Graveyard: You can banish a total of 2 WATER and/or Dragon-Type monsters from your hand and/or Graveyard, except this card; Special Summon this card. During your opponent's End Phase, if this card was Special Summoned: Return it to the hand. You can discard this card and 1 WATER monster to the Graveyard; send 1 monster from your Deck to the Graveyard. If this card is banished: You can add 1 WATER Dragon-Type monster from your Deck to your hand. You can only use 1 'Tidal, Dragon Ruler of Waterfalls' effect per turn, and only once that turn.",
	'Dragon/Effect', 2600, 2000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'33184167', (select color_id from card_colors where card_color = 'Effect'), 'Tribe-Infecting Virus', 'Water',
	"Discard 1 card from your hand and declare 1 Type of monster. Destroy all face-up monsters of the declared Type on the field.",
	'Aqua/Effect', 1600, 1000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'30539496', (select color_id from card_colors where card_color = 'Effect'), 'True King Lithosagym, the Disaster', 'Earth',
	"If this card is in your hand: You can destroy 2 other monsters in your hand and/or face-up on your field, including an EARTH monster, and if you do, Special Summon this card, and if you do that, and both destroyed monsters were EARTH, you can also look at your opponent's Extra Deck and banish up to 3 monsters from it with different names. If this card is destroyed by card effect: You can Special Summon 1 non-EARTH Wyrm-Type monster from your Graveyard. You can only use each effect of 'True King Lithosagym, the Disaster' once per turn.",
	'Wyrm/Effect', 2500, 2300
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'44910027', (select color_id from card_colors where card_color = 'Effect'), 'Victory Dragon', 'Dark',
	"This card cannot be Special Summoned. To Tribute Summon this card, you must Tribute 3 Dragon-Type monsters. If this card attacks your opponent directly and reduces their Life Points to 0, you win the Match.",
	'Dragon/Effect', 2400, 3000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'03078576', (select color_id from card_colors where card_color = 'Effect'), 'Yata-Garasu', 'Wind',
	"This card cannot be Special Summoned. This card returns to its owner's hand during the End Phase of the turn it is Normal Summoned or flipped face-up. When this card inflicts Battle Damage to your opponent, they skip their next Draw Phase.",
	'Fiend/Spirit/Effect', 200, 100
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'09411399', (select color_id from card_colors where card_color = 'Effect'), 'Destiny HERO - Malicious', 'Dark',
	"You can banish this card from your GY; Special Summon 1 'Destiny HERO - Malicious' from your Deck.",
	'Warrior/Effect', 800, 800
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'28297833', (select color_id from card_colors where card_color = 'Effect'), 'Necroface', 'Dark',
	"If this card is Normal Summoned: Shuffle all banished cards into the Deck. This card gains 100 ATK for each card shuffled into the Main Deck by this effect. If this card is banished: Each player banishes 5 cards from the top of their Deck (or their entire Deck, if less than 5).",
	'Zombie/Effect', 1200, 1800
);