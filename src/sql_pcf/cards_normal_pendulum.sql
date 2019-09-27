USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'65518099', (select color_id from card_colors where card_color = 'Pendulum-Normal'), 'Qliphort Scout', 'Earth',
	"Pendulum EffectYou cannot Special Summon monsters, except 'Qli' monsters. This effect cannot be negated. Once per turn: You can pay 800 LP; add 1 'Qli' card from your Deck to your hand, except 'Qliphort Scout'.
Monster EffectBooting in Replica Mode…
An error has occurred while executing C:\sophia\zefra.exe
Unknown publisher.
Allow C:\tierra\qliphort.exe ? <Y/N>…[Y]
Booting in Autonomy Mode…)"
	, 'Machine/Pendulum/Normal', 1000, 2800
);