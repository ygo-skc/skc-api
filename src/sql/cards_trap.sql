USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name,card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'28566710', (select color_id from card_colors where card_color = 'Trap'), 'Last Turn', 'Trap',
	"This card can only be activated during your opponent's turn when your Life Points are 1000 or less. Select 1 monster on your side of the field and send all other cards on the field and in their respective owner's hands to their respective Graveyards. After that, your opponent selects and Special Summons 1 monster from their Deck in face-up Attack Position and attacks your selected monster. (Any Battle Damage from this battle is treated as 0.) The player whose monster remains alone on the field at the End Phase of this turn wins the Duel. Any other case results in a DRAW."
	, null, null, null
);