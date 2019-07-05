USE yugioh_API_DB;

DROP TABLE cards;
DROP TABLE card_colors;

CREATE TABLE card_colors
(
	color_id INT NOT NULL AUTO_INCREMENT,
	card_color VARCHAR(15) NOT NULL,
	PRIMARY KEY(color_id)
);

CREATE TABLE cards
(
	card_number CHAR(8) NOT NULL,
	color_id INT NOT NULL,
	card_name VARCHAR(40) NOT NULL,
	card_attribute VARCHAR(20) NOT NULL,
	card_type VARCHAR(25) NOT NULL,
	card_effect VARCHAR(1500),
	monster_type VARCHAR(30),
	monster_attack INT,
	monster_defense INT,
	PRIMARY KEY(card_number),
	FOREIGN KEY(color_id) REFERENCES card_colors(color_id)
);

INSERT INTO card_colors(card_color) VALUES('Normal');
INSERT INTO card_colors(card_color) VALUES('Effect');
INSERT INTO card_colors(card_color) VALUES('Fusion');
INSERT INTO card_colors(card_color) VALUES('Ritual');
INSERT INTO card_colors(card_color) VALUES('Synchro');
INSERT INTO card_colors(card_color) VALUES('XYZ');
INSERT INTO card_colors(card_color) VALUES('Pendulum');
INSERT INTO card_colors(card_color) VALUES('Link');

INSERT INTO card_colors(card_color) VALUES('Spell');
INSERT INTO card_colors(card_color) VALUES('Trap');

INSERT INTO cards(
	card_number, color_id, card_name,
	card_attribute, card_type,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'76794549', 7, 'Astrograph Sorcerer',
	'Dark', 'Monster',
	"Pendulum EffectDuring your Main Phase: You can destroy this card, and if you do, take 1 ;Stargazer Magician; from your hand or Deck, and either place it in your Pendulum Zone or Special Summon it. You can only use this effect of ;Astrograph Sorcerer; once per turn.
Monster EffectIf a card(s) you control is destroyed by battle or card effect: You can Special Summon this card from your hand, then you can choose 1 monster in the Graveyard, Extra Deck, or that is banished, and that was destroyed this turn, and add 1 monster with the same name from your Deck to your hand. You can banish this card you control, plus 4 monsters from your hand, field, and/or Graveyard (1 each with 'Pendulum Dragon', 'Xyz Dragon', 'Synchro Dragon', and 'Fusion Dragon' in their names); Special Summon 1 'Supreme King Z-ARC' from your Extra Deck. (This is treated as a Fusion Summon.)"
	, 'Spellcaster/Pendulum/Effect', 2500, 2000
);


INSERT INTO cards(
        card_number, color_id, card_name,
        card_attribute, card_type,
        card_effect,
        monster_type, monster_attack, monster_defense)
VALUES (
        '09929398', 2, 'Blackwing - Gofu the Vague Shadow',
        'Dark', 'Monster',
	"Cannot be Normal Summoned/Set. Must first be Special Summoned (from your hand) while you control no monsters. When this card is Special Summoned from the hand: You can Special Summon 2 'Vague Shadow Tokens' (Winged Beast-Type/DARK/Level 1/ATK 0/DEF 0), but they cannot be Tributed or be used as Synchro Material. You can banish this card and 1 or more face-up non-Tuners you control, then target 1 'Blackwing' Synchro Monster in your Graveyard whose Level equals the total Levels the banished monsters had on the field; Special Summon it, and if you do, it is treated as a Tuner."
        , 'Winged-Beast/Tuner/Effect', 0, 0
);


INSERT INTO cards(
        card_number, color_id, card_name,
        card_attribute, card_type,
        card_effect,
        monster_type, monster_attack, monster_defense)
VALUES (
        '53804307', 2, 'Blaster, Dragon Ruler of Infernos',
        'Fire', 'Monster',
	"If this card is in your hand or Graveyard: You can banish a total of 2 FIRE and/or Dragon-Type monsters from your hand and/or Graveyard, except this card; Special Summon this card. During your opponent's End Phase, if this card was Special Summoned: Return it to the hand. You can discard this card and 1 FIRE monster to the Graveyard, then target 1 card on the field; destroy that target. If this card is banished: You can add 1 FIRE Dragon-Type monster from your Deck to your hand. You can only use 1 'Blaster, Dragon Ruler of Infernos' effect per turn, and only once that turn."
        , 'Dragon/Effect', 2800, 1800
);

INSERT INTO cards(
        card_number, color_id, card_name,
        card_attribute, card_type,
        card_effect,
        monster_type, monster_attack, monster_defense)
VALUES (
        '7412721', 3, 'Elder Entity Norden',
        'Water', 'Monster',
	"1 Synchro or Xyz Monster + 1 Synchro or Xyz Monster
When this card is Special Summoned: You can target 1 Level 4 or lower monster in your Graveyard; Special Summon it, but its effects are negated, also banish it when this card leaves the field."
        , 'Fairy/Fusion/Effect', 2000, 2200
);

INSERT INTO cards(
        card_number, color_id, card_name,
        card_attribute, card_type,
        card_effect,
        monster_type, monster_attack, monster_defense)
VALUES (
        '25862681', 5, 'Ancient Fairy Dragon',
        'Light', 'Monster',
	"1 Tuner + 1+ non-Tuner monsters
Once per turn: You can Special Summon 1 Level 4 or lower monster from your hand. You cannot conduct your Battle Phase the turn you activate this effect. Once per turn: You can destroy as many Field Spells on the field as possible, and if you do, gain 1000 LP, then you can add 1 Field Spell from your Deck to your hand."
        , 'Dragon/Synchro/Effect', 2100, 3000
);

INSERT INTO cards(
        card_number, color_id, card_name,
        card_attribute, card_type,
        card_effect,
        monster_type, monster_attack, monster_defense)
VALUES (
        '00581014', 6, 'Daigusto Emeral',
        'Wind', 'Monster',
	"2 Level 4 monsters
Once per turn: You can detach 1 Xyz Material from this card, then activate 1 of these effects.
● Target 3 monsters in your Graveyard; shuffle all 3 into the Deck, then draw 1 card.
● Target 1 non-Effect Monster in your Graveyard; Special Summon that target."
        , 'Rock/Xyz/Effect', 1800, 800
);

INSERT INTO cards(
        card_number, color_id, card_name,
        card_attribute, card_type,
        card_effect,
        monster_type, monster_attack, monster_defense)
VALUES (
        '05043010', 8, 'Firewall Dragon',
        'Light', 'Monster',
	"2+ monsters
Once while face-up on the field (Quick Effect): You can target monsters on the field and/or GY up to the number of monsters co-linked to this card; return them to the hand. If a monster this card points to is destroyed by battle or sent to the GY: You can Special Summon 1 monster from your hand."
        , 'Cyberse/Link/Effect', 2500, null
);

/*
	SPELLS
*/
INSERT INTO cards(
        card_number, color_id, card_name,
        card_attribute, card_type,
        card_effect,
        monster_type, monster_attack, monster_defense)
VALUES (
        '69243953', 9, 'Butterfly Dagger - Elma',
        'Spell', 'Spell',
	"The equipped monster gains 300 ATK. When this card is destroyed and sent to the Graveyard while equipped: You can return this card to the hand."
        , null, null, null
);

/*
	Traps
*/
INSERT INTO cards(
        card_number, color_id, card_name,
        card_attribute, card_type,
        card_effect,
        monster_type, monster_attack, monster_defense)
VALUES (
        '28566710', 10, 'Last Turn',
        'Trap', 'Trap',
	"This card can only be activated during your opponent's turn when your Life Points are 1000 or less. Select 1 monster on your side of the field and send all other cards on the field and in their respective owner's hands to their respective Graveyards. After that, your opponent selects and Special Summons 1 monster from their Deck in face-up Attack Position and attacks your selected monster. (Any Battle Damage from this battle is treated as 0.) The player whose monster remains alone on the field at the End Phase of this turn wins the Duel. Any other case results in a DRAW."
        , null, null, null
);
