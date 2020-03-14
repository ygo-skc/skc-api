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
	monster_type, monster_attack, monster_defense)
	VALUES (
		'40044918', 1, 'Elemental HERO Stratos', 'Wind',
		'When this card is Normal or Special Summoned: You can activate 1 of these effects.
&bull; You can destroy Spell/Trap Cards on the field, up to the number of "HERO" monsters you control, except this card.
&bull; Add 1 "HERO" monster from your Deck to your hand.',
		'Warrior/Effect', 1800, 300
	);