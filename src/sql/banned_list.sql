CREATE DATABASE yugioh_API_DB;
USE yugioh_API_DB;

DROP TABLE ban_list;

CREATE TABLE ban_list
(
	card_number CHAR(10),
	card_name VARCHAR(25),
	card_effect VARCHAR(100),
	PRIMARY KEY(card_number)
);


