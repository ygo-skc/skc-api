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


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense, monster_association)
	VALUES (
		'40044918', (select color_id from card_colors where card_color = 'Effect'), 'Elemental HERO Stratos', 'Wind',
		'When this card is Normal or Special Summoned: You can activate 1 of these effects.
&bull; You can destroy Spell/Trap Cards on the field, up to the number of "HERO" monsters you control, except this card.
&bull; Add 1 "HERO" monster from your Deck to your hand.',
		'Warrior/Effect', 1800, 300, '{"level": "4"}'
	)
	, (
		'09411399', (select color_id from card_colors where card_color = 'Effect'), 'Destiny HERO - Malicious', 'Dark',
		'You can banish this card from your GY; Special Summon 1 "Destiny HERO - Malicious" from your Deck.',
		'Warrior/Effect', 800, 800, '{"level": "6"}'
	);



INSERT INTO cards (
	card_number, color_id, card_name, card_attribute,
	card_effect, property)
    VALUES (
        '08949584', (select color_id from card_colors where card_color = 'Spell'), 'A Hero Lives', 'Spell',
        'If you control no face-up monsters: Pay half your LP; Special Summon 1 Level 4 or lower "Elemental HERO" monster from your Deck.'
        , null
    );


INSERT INTO ban_lists(ban_list_date, card_number, ban_status)
	VALUES ('2015-11-09', '40044918', 'Forbidden')
	, ('2015-11-09', '08949584', 'Limited')
	, ('2015-11-09', '09411399', 'Limited');