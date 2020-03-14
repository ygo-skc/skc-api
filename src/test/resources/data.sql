DROP TABLE IF EXISTS pack_details;
DROP TABLE IF EXISTS packs;
DROP TABLE IF EXISTS ban_lists;
DROP TABLE IF EXISTS cards;
DROP TABLE IF EXISTS card_colors;

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
	card_name VARCHAR(50) NOT NULL,
	card_attribute VARCHAR(20) NOT NULL,
	card_effect VARCHAR(1500),
	monster_type VARCHAR(35),
	monster_attack INT,
	monster_defense INT,
	PRIMARY KEY(card_number),
	FOREIGN KEY(color_id) REFERENCES card_colors(color_id),
	INDEX(card_number(3)
		, monster_type(6)
		, card_attribute(1)
		, card_name(7))
);

CREATE TABLE ban_lists
(
	ban_list_date DATE NOT NULL,
	card_number CHAR(8) NOT NULL,
	ban_status VARCHAR(15) NOT NULL,
	PRIMARY KEY(ban_list_date, card_number),
	FOREIGN KEY(card_number) REFERENCES cards(card_number),
	INDEX(card_number(1), ban_status(1))
);

INSERT INTO card_colors(card_color)
	VALUES	('Normal')
				, ('Effect')
				, ('Fusion')
				, ('Ritual')
				, ('Synchro')
				, ('XYZ')
				, ('Pendulum-Normal')
				, ('Pendulum-Effect')
				, ('Link')
				, ('Spell')
				, ('Trap');

CREATE TABLE packs
(
	pack_id VARCHAR(9),
	pack_name VARCHAR(50) NOT NULL,
	pack_release_date DATE NOT NULL,
	PRIMARY KEY(pack_id)
);

CREATE TABLE pack_details
(
	pack_id VARCHAR(9),
	card_pack_position varchar(3) NOT NULL,
	card_number VARCHAR(8) NOT NULL,
	PRIMARY KEY(pack_id, card_pack_position),
	FOREIGN KEY(pack_id) REFERENCES packs(pack_id),
	FOREIGN KEY(card_number) REFERENCES cards(card_number)
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
	VALUES (
		'40044918', (select color_id from card_colors where card_color = 'Effect'), 'Elemental HERO Javi', 'Wind',
		"When this card is Normal or Special Summoned: You can activate 1 of these effects.
&bull; You can destroy Spell/Trap Cards on the field, up to the number of 'HERO' monsters you control, except this card.
&bull; Add 1 'HERO' monster from your Deck to your hand.",
		'Warrior/Effect', 1800, 300
	)