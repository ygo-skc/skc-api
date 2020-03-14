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
	FOREIGN KEY(color_id) REFERENCES card_colors(color_id)
);

CREATE TABLE ban_lists
(
	ban_list_date DATE NOT NULL,
	card_number CHAR(8) NOT NULL,
	ban_status VARCHAR(15) NOT NULL,
	PRIMARY KEY(ban_list_date, card_number),
	FOREIGN KEY(card_number) REFERENCES cards(card_number)
);

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

CREATE VIEW card_info
AS
SELECT
	card_number, card_colors.card_color, card_name, cards.card_attribute, cards.card_effect, cards.monster_type, cards.monster_attack, cards.monster_defense
FROM
	cards, card_colors
WHERE
	card_colors.color_id = cards.color_id;

CREATE VIEW ban_list_info
AS
SELECT
	card_name, monster_type, card_color, card_effect, card_info.card_number, ban_status, ban_list_date
FROM
	card_info, ban_lists
WHERE
	card_info.card_number = ban_lists.card_number;