USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'76794549', (select color_id from card_colors where card_color = 'Pendulum'), 'Astrograph Sorcerer', 'Dark',
	"Pendulum EffectDuring your Main Phase: You can destroy this card, and if you do, take 1 ;Stargazer Magician; from your hand or Deck, and either place it in your Pendulum Zone or Special Summon it. You can only use this effect of ;Astrograph Sorcerer; once per turn.
Monster EffectIf a card(s) you control is destroyed by battle or card effect: You can Special Summon this card from your hand, then you can choose 1 monster in the Graveyard, Extra Deck, or that is banished, and that was destroyed this turn, and add 1 monster with the same name from your Deck to your hand. You can banish this card you control, plus 4 monsters from your hand, field, and/or Graveyard (1 each with 'Pendulum Dragon', 'Xyz Dragon', 'Synchro Dragon', and 'Fusion Dragon' in their names); Special Summon 1 'Supreme King Z-ARC' from your Extra Deck. (This is treated as a Fusion Summon.)"
	, 'Spellcaster/Pendulum/Effect', 2500, 2000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'49684352', (select color_id from card_colors where card_color = 'Pendulum'), 'Double Iris Magician', 'Dark',
	"Pendulum EffectOnce per turn: You can target 1 DARK Spellcaster-Type monster you control; apply this effect to it, then destroy this card. Once applied, double any battle damage that monster inflicts to your opponent this turn if it battles an opponent's monster.
Monster Effect(This card is always treated as a 'Pendulum Dragon' card.)
If this card is destroyed by battle or card effect: You can add 1 'Pendulumgraph' card from your Deck to your hand."
	, 'Spellcaster/Pendulum/Effect', 1500, 1000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'31178212', (select color_id from card_colors where card_color = 'Pendulum'), 'Majespecter Unicorn - Kirin', 'Wind',
	"During either player's turn: You can target 1 Pendulum Monster in your Monster Zone and 1 monster your opponent controls; return them to the hand(s). You can only use this effect of 'Majespecter Unicorn - Kirin' once per turn. Cannot be targeted or destroyed by your opponent's card effects.
"
	, 'Spellcaster/Pendulum/Effect', 2000, 2000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'07563579', (select color_id from card_colors where card_color = 'Pendulum'), 'Performage Plushfire', 'Fire',
	"Pendulum EffectIf a 'Performage' monster(s) you control is destroyed by battle or card effect: You can Special Summon this card from your Pendulum Zone, then take 500 damage. You can only use this effect of 'Performage Plushfire' once per turn.
Monster EffectIf this card on the field is destroyed by battle or card effect: You can Special Summon 1 'Performage' monster from your hand or Deck, except 'Performage Plushfire'."
	, 'Spellcaster/Pendulum/Effect', 1000, 1000
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'17330916', (select color_id from card_colors where card_color = 'Pendulum'), 'Performapal Monkeyboard', 'Earth',
	"Pendulum Effect
Unless you have a 'Performapal' card in your other Pendulum Zone, this card's Pendulum Scale becomes 4. During your Main Phase, if this card was activated this turn: You can add 1 Level 4 or lower 'Performapal' monster from your Deck to your hand. You can only use this effect of 'Performapal Monkeyboard' once per turn.
Monster Effect
You can discard this card; reveal 1 'Performapal' or 'Odd-Eyes' monster in your hand, and if you do, reduce the Levels of monsters in your hand with that name by 1 for the rest of this turn (even after they are Summoned/Set)."
	, 'Beast/Pendulum/Effect', 1000, 2400
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'40318957', (select color_id from card_colors where card_color = 'Pendulum'), 'Performapal Skullcrobat Joker', 'Dark',
	"Pendulum Effect
You cannot Pendulum Summon monsters, except 'Performapal' monsters, 'Magician' Pendulum Monsters, and 'Odd-Eyes' monsters. This effect cannot be negated.
Monster Effect
When this card is Normal Summoned: You can add 1 'Performapal' monster, 'Magician' Pendulum Monster, or 'Odd-Eyes' monster from your Deck to your hand, except 'Performapal Skullcrobat Joker'."
	, 'Spellcaster/Pendulum/Effect', 1800, 100
);


INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
VALUES (
	'92746535', (select color_id from card_colors where card_color = 'Pendulum'), 'Luster Pendulum, the Dracoslayer', 'Light',
	"Pendulum EffectOnce per turn, if you have a card in your other Pendulum Zone: You can destroy that card, and if you do, add 1 card from your Deck to your hand, with the same name as that card.
Monster EffectCannot Special Summon Fusion, Synchro or Xyz Monsters using this card as a Material, except 'Dracoslayer' monsters."
	, 'Dragon/Pendulum/Tuner/Effect', 1850, 0
);