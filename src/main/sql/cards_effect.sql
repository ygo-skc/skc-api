USE yugioh_API_DB;

INSERT INTO cards(
	card_number, color_id, card_name, card_attribute,
	card_effect,
	monster_type, monster_attack, monster_defense)
	VALUES (
		'30012506', (select color_id from card_colors where card_color = 'Effect'), 'A-Assault Core', 'Light',
		"Once per turn, you can either: Target 1 LIGHT Machine monster you control; equip this card to that target, OR: Unequip this card and Special Summon it. A monster equipped with this card is unaffected by your opponent's monster effects (except its own), also if the equipped monster would be destroyed by battle or card effect, destroy this card instead. If this card is sent from the field to the GY: You can add 1 other Union monster from your GY to your hand.",
		'Machine/Union/Effect', 1900, 200
	)
	, (
		'27279764', (select color_id from card_colors where card_color = 'Effect'), 'Apoqliphort Towers', 'Earth',
		"Cannot be Special Summoned. Requires 3 'Qli' Tributes to Normal Summon/Set. If this card is Normal Summoned/Set, it is unaffected by Spell/Trap effects and by activated effects from any monster whose original Level/Rank is lower than this card's current Level. All Special Summoned monsters lose 500 ATK and DEF. Once per turn: You can make your opponent send 1 monster from their hand or their side of the field to the Graveyard (their choice).",
		'Machine/Effect', 3000, 2600
	)
	, (
		'85103922', (select color_id from card_colors where card_color = 'Effect'), 'Artifact Moralltach', 'Light',
		"You can Set this card from your hand to your Spell & Trap Zone as a Spell Card. During your opponent's turn, if this Set card in the Spell & Trap Zone is destroyed and sent to your Graveyard: Special Summon it. If this card is Special Summoned during your opponent's turn: You can destroy 1 face-up card your opponent controls.",
		'Fairy/Effect', 2100, 1400
	)
	, (
		'74311226', (select color_id from card_colors where card_color = 'Effect'), 'Atlantean Dragoons', 'Water',
		"All Level 3 or lower Sea Serpent-Type monsters you control can attack your opponent directly. When this card is sent to the Graveyard to activate a WATER monster's effect: Add 1 Sea Serpent-Type monster from your Deck to your hand, except 'Atlantean Dragoons'.",
		'Sea-Serpent/Effect', 1800, 0
	)
	, (
		'72989439', (select color_id from card_colors where card_color = 'Effect'), 'Black Luster Soldier - Envoy of the Beginning', 'Light',
		"Cannot be Normal Summoned/Set. Must first be Special Summoned (from your hand) by banishing 1 LIGHT and 1 DARK monster from your Graveyard. Once per turn, you can activate 1 of these effects.
&bull; Target 1 monster on the field; banish that target face-up. This card cannot attack the turn this effect is activated.
&bull; If this attacking card destroys an opponent's monster by battle, after damage calculation: It can make a second attack in a row.",
		'Warrior/Effect', 3000, 2500
	)
	, (
		'09929398', (select color_id from card_colors where card_color = 'Effect'), 'Blackwing - Gofu the Vague Shadow', 'Dark',
		"Cannot be Normal Summoned/Set. Must first be Special Summoned (from your hand) while you control no monsters. When this card is Special Summoned from the hand: You can Special Summon 2 'Vague Shadow Tokens' (Winged Beast-Type/DARK/Level 1/ATK 0/DEF 0), but they cannot be Tributed or be used as Synchro Material. You can banish this card and 1 or more face-up non-Tuners you control, then target 1 'Blackwing' Synchro Monster in your Graveyard whose Level equals the total Levels the banished monsters had on the field; Special Summon it, and if you do, it is treated as a Tuner.",
		'Winged-Beast/Tuner/Effect', 0, 0
	)
	, (
		'53804307', (select color_id from card_colors where card_color = 'Effect'), 'Blaster, Dragon Ruler of Infernos', 'Fire',
		"If this card is in your hand or Graveyard: You can banish a total of 2 FIRE and/or Dragon-Type monsters from your hand and/or Graveyard, except this card; Special Summon this card. During your opponent's End Phase, if this card was Special Summoned: Return it to the hand. You can discard this card and 1 FIRE monster to the Graveyard, then target 1 card on the field; destroy that target. If this card is banished: You can add 1 FIRE Dragon-Type monster from your Deck to your hand. You can only use 1 'Blaster, Dragon Ruler of Infernos' effect per turn, and only once that turn.",
		'Dragon/Effect', 2800, 1800
	)
	, (
		'85087012', (select color_id from card_colors where card_color = 'Effect'), 'Card Trooper', 'Earth',
		"Once per turn: You can choose a number from 1 to 3, then send that many cards from the top of your Deck to the GY; this card gains 500 ATK for each card sent to the GY this way, until the end of this turn. If this card you control is destroyed and sent to your GY: Draw 1 card.",
		'Machine/Effect', 400, 400
	)
	, (
		'34124316', (select color_id from card_colors where card_color = 'Effect'), 'Cyber Jar', 'Dark',
		"FLIP: Destroy all monsters on the field, then both players reveal the top 5 cards from their Decks, then Special Summon all revealed Level 4 or lower monsters in face-up Attack Position or face-down Defense Position, also add any remaining cards to their hand. (If either player has less than 5 cards in their Deck, reveal as many as possible.)",
		'Rock/Effect', 900, 900
	)
	, (
		'15341821', (select color_id from card_colors where card_color = 'Effect'), 'Dandylion', 'Earth',
		"If this card is sent to the Graveyard: Special Summon 2 'Fluff Tokens' (Plant-Type/WIND/Level 1/ATK 0/DEF 0) in Defense Position. These Tokens cannot be Tributed for a Tribute Summon during the turn they are Special Summoned.",
		'Plant/Effect', 300, 300
	)
	, (
		'40737112', (select color_id from card_colors where card_color = 'Effect'), 'Dark Magician of Chaos', 'Dark',
		"During the End Phase, if this card was Normal or Special Summoned this turn: You can target 1 Spell in your GY; add it to your hand. You can only use this effect of 'Dark Magician of Chaos' once per turn. If this card destroys an opponent's monster by battle, after damage calculation: Banish that opponent's monster. If this face-up card would leave the field, banish it instead.",
		'Spellcaster/Effect', 2800, 2600
	)
	, (
		'14943837', (select color_id from card_colors where card_color = 'Effect'), 'Debris Dragon', 'Wind',
		"When this card is Normal Summoned: You can target 1 monster with 500 or less ATK in your GY; Special Summon that target in Attack Position, but it has its effects negated. Cannot be used as Synchro Material, except for the Synchro Summon of a Dragon monster. The other Synchro Material(s) cannot be Level 4.",
		'Dragon/Effect', 1000, 2000
	)
	, (
		'56570271', (select color_id from card_colors where card_color = 'Effect'), 'Destiny HERO - Disk Commander', 'Dark',
		"Cannot be Special Summoned from the GY the turn this card was sent to the GY. If this card is Special Summoned from the GY: You can draw 2 cards. You can only use this effect of 'Destiny HERO - Disk Commander' once per Duel.",
		'Warrior/Effect', 300, 300
	)
	, (
		'08903700', (select color_id from card_colors where card_color = 'Effect'), 'Djinn Releaser of Rituals', 'Dark',
		"When you Ritual Summon a monster, you can banish this card from your Graveyard as 1 of the monsters required for the Ritual Summon. If a player Ritual Summons using this card, the other player cannot Special Summon while that Ritual Summoned monster is face-up on the field.",
		'Fiend/Effect', 1200, 2000
	)
	, (
		'55623480', (select color_id from card_colors where card_color = 'Effect'), 'Fairy Tail - Snow', 'Light',
		"If this card is Normal or Special Summoned: You can target 1 face-up monster your opponent controls; change it to face-down Defense Position. If this card is in your GY (Quick Effect): You can banish 7 other cards from your hand, field, and/or GY; Special Summon this card.",
		'Spellcaster/Effect', 1850, 1000
	)
	, (
		'78706415', (select color_id from card_colors where card_color = 'Effect'), 'Fiber Jar', 'Earth',
		"FLIP: Each player shuffles all cards from their hand, field, and Graveyard into the Deck, then draws 5 cards.",
		'Plant/Effect', 500, 500
	)
	, (
		'93369354', (select color_id from card_colors where card_color = 'Effect'), 'Fishborg Blaster', 'Water',
		"If you control a face-up Level 3 or lower WATER monster: You can discard 1 card; Special Summon this card from your Graveyard. If this card is used as a Synchro Material Monster, all other Synchro Material Monsters must be WATER.",
		'Fish/Tuner/Effect', 100, 200
	)
	, (
		'61468779', (select color_id from card_colors where card_color = 'Effect'), 'Grandsoil the Elemental Lord', 'Earth',
		"Cannot be Normal Summoned/Set. Must be Special Summoned (from your hand) by having exactly 5 EARTH monsters in your GY. When this card is Special Summoned: You can target 1 monster in either player's GY; Special Summon that target. You can only use this effect of 'Grandsoil the Elemental Lord' once per turn. If this face-up card leaves the field, skip the Battle Phase of your next turn.",
		'Beast-Warrior/Effect', 2800, 2200
	)
	, (
		'75732622', (select color_id from card_colors where card_color = 'Effect'), 'Grinder Golem', 'Dark',
		"Cannot be Normal Summoned or Set. Must first be Special Summoned (from your hand) to your opponent's side of the field by Special Summoning 2 'Grinder Tokens' (Fiend-Type/DARK/Level 1/ATK 0/DEF 0) in face-up Attack Position on your side of the field. If you Special Summon this monster, you cannot Normal Summon or Set a monster during the same turn.",
		'Fiend/Effect', 3000, 300
	)
	, (
		'37742478', (select color_id from card_colors where card_color = 'Effect'), 'Honest', 'Light',
		"During your Main Phase: You can return this face-up card from the field to the hand. During the Damage Step, when a LIGHT monster you control battles (Quick Effect): You can send this card from your hand to the GY; that monster gains ATK equal to the ATK of the opponent's monster it is battling, until the end of this turn.",
		'Fairy/Effect', 1100, 1900
	)
	, (
		'22499034', (select color_id from card_colors where card_color = 'Effect'), 'Ignis Heat, the True Dracowarrior', 'Fire',
		"To Tribute Summon this card face-up, you can Tribute a Continuous Spell/Trap Card you control, instead of a monster. Once per turn, during either player's turn, when your opponent activates a card or effect while you control this Tribute Summoned monster: You can take 1 'True Draco' or 'True King' Continuous Spell Card from your Deck, and either activate it or add it to your hand.",
		'Wyrm/Effect', 2400, 1000
	)
	, (
		'55885348', (select color_id from card_colors where card_color = 'Effect'), 'Kozmo Dark Destroyer', 'Dark',
		"If this card is Normal or Special Summoned: You can target 1 monster on the field; destroy it. Cannot be targeted by an opponent's card effects. If this card is destroyed by battle or card effect and sent to the Graveyard: You can banish this card from your Graveyard; Special Summon 1 Level 7 or lower 'Kozmo' monster from your Deck.",
		'Machine/Effect', 3000, 1800
	)
	, (
		'57421866', (select color_id from card_colors where card_color = 'Effect'), 'Level Eater', 'Dark',
		"If this card is in your Graveyard: You can target 1 Level 5 or higher monster you control; reduce its Level by 1, and if you do, Special Summon this card. This face-up card on the field cannot be Tributed, except for a Tribute Summon.",
		'Insect/Effect', 600, 0
	)
	, (
		'34206604', (select color_id from card_colors where card_color = 'Effect'), 'Magical Scientist', 'Dark',
		"Pay 1000 Life Points to Special Summon 1 level 6 or lower Fusion Monster from your Extra Deck in face-up Attack or Defense Position. That Fusion Monster cannot attack your opponent's Life Points directly, and is returned to your Extra Deck at the end of the turn.",
		'Spellcaster/Effect', 300, 300
	)
	, (
		'21593977', (select color_id from card_colors where card_color = 'Effect'), 'Makyura the Destructor', 'Dark',
		"During the turn this card was sent to the Graveyard, you can activate Trap Cards from your hand.",
		'Warrior/Effect', 1600, 1200
	)
	, (
		'21377582', (select color_id from card_colors where card_color = 'Effect'), 'Master Peace, the True Dracoslaying King', 'Light',
		"To Tribute Summon this card face-up, you can Tribute Continuous Spell/Trap Card(s) you control, as well as monsters. Unaffected by the effects of cards with the same card type (Monster, Spell, and/or Trap) as the original card type of the cards Tributed for its Tribute Summon. Once per turn, during either player's turn, if you control this Tribute Summoned monster: You can banish 1 Continuous Spell/Trap Card from your Graveyard, then target 1 other card on the field; destroy it.",
		'Wyrm/Effect', 2950, 2950
	)
	, (
		'41386308', (select color_id from card_colors where card_color = 'Effect'), 'Mathematician', 'Earth',
		"When this card is Normal Summoned: You can send 1 Level 4 or lower monster from your Deck to the GY. When this card is destroyed by battle and sent to the GY: You can draw 1 card.",
		'Spellcaster/Effect', 2500, 500
	)
	, (
		'23434538', (select color_id from card_colors where card_color = 'Effect'), "Maxx 'C'", 'Earth',
		"During either player's turn: You can send this card from your hand to the Graveyard; this turn, each time your opponent Special Summons a monster(s), immediately draw 1 card. You can only use 1 'Maxx 'C'''champ per turn.",
		'Insect/Effect', 500, 200
	)
	, (
		'96782886', (select color_id from card_colors where card_color = 'Effect'), 'Mind Master', 'Light',
		"You can pay 800 Life Points and Tribute 1 Psychic-Type monster, except 'Mind Master', to Special Summon 1 Level 4 or lower Psychic-Type monster from your Deck in face-up Attack Position.",
		'Psychic/Effect', 100, 200
	)
	, (
		'79106360', (select color_id from card_colors where card_color = 'Effect'), 'Morphing Jar #2', 'Earth',
		"FLIP: Shuffle all monsters on the field into the Deck. Then, each player excavates cards from the top of their Deck, until they excavate the same number of monsters they shuffled into their Main Deck. Special Summon all excavated Level 4 or lower monsters in face-down Defense Position, also send the remaining cards to the Graveyard.",
		'Rock/Effect', 800, 700
	)
	, (
		'80344569', (select color_id from card_colors where card_color = 'Effect'), 'Neo-Spacian Grand Mole', 'Earth',
		"At the start of the Damage Step, if this card battles an opponent's monster: You can return both the opponent's monster and this card to the hand.",
		'Rock/Effect', 900, 300
	)
	, (
		'57835716', (select color_id from card_colors where card_color = 'Effect'), 'Orcust Harp Horror', 'Dark',
		"You can banish this card from your GY; Special Summon 1 'Orcust' monster from your Deck, except 'Orcust Harp Horror', also you cannot Special Summon monsters for the rest of this turn, except DARK monsters. You can only use this effect of 'Orcust Harp Horror' once per turn.",
		'Machine/Effect', 1700, 1400
	)
	, (
		'23558733', (select color_id from card_colors where card_color = 'Effect'), 'Phoenixian Cluster Amaryllis', 'Fire',
		"This card cannot be Special Summoned except with its own effect or with 'Phoenixian Seed'. If this card attacks, it is destroyed after damage calculation. If this card you control is destroyed and sent to the Graveyard, inflict 800 damage to your opponent. During your End Phase, you can remove from play 1 Plant-Type monster from your Graveyard to Special Summon this card from your Graveyard in Defense Position.",
		'Rock/Effect', 2300, 0
	)
	, (
		'90411554', (select color_id from card_colors where card_color = 'Effect'), 'Redox, Dragon Ruler of Boulders', 'Earth',
		"If this card is in your hand or Graveyard: You can banish a total of 2 EARTH and/or Dragon-Type monsters from your hand and/or Graveyard, except this card; Special Summon this card. During your opponent's End Phase, if this card was Special Summoned: Return it to the hand. You can discard this card and 1 EARTH monster to the Graveyard, then target 1 monster in your Graveyard; Special Summon that target. If this card is banished: You can add 1 EARTH Dragon-Type monster from your Deck to your hand. You can only use 1 'Redox, Dragon Ruler of Boulders' effect per turn, and only once that turn.",
		'Dragon/Effect', 1600, 3000
	)
	, (
		'14878871', (select color_id from card_colors where card_color = 'Effect'), 'Rescue Cat', 'Earth',
		"You can send this card to the Graveyard; Special Summon 2 Level 3 or lower Beast-Type monsters from your Deck, but they have their effects negated, also they are destroyed during the End Phase. You can only use this effect of 'Rescue Cat' once per turn.",
		'Beast/Effect', 300, 100
	)
	, (
		'85138716', (select color_id from card_colors where card_color = 'Effect'), 'Rescue Rabbit', 'Earth',
		"Cannot be Special Summoned from the Deck. You can banish this face-up card you control; Special Summon 2 Level 4 or lower Normal Monsters with the same name from your Deck, but destroy them during the End Phase. You can only use this effect of 'Rescue Rabbit' once per turn.",
		'Beast/Effect', 300, 100
	)
	, (
		'05592689', (select color_id from card_colors where card_color = 'Effect'), 'Samsara Lotus', 'Dark',
		"During your End Phase, if you control no Spell or Trap Cards, you can Special Summon this card from your Graveyard in face-up Attack Position. This card's controller takes 1000 damage during each of their Standby Phases.",
		'Plant/Effect', 0, 0
	)
	, (
		'26202165', (select color_id from card_colors where card_color = 'Effect'), 'Sangan', 'Dark',
		"If this card is sent from the field to the GY: Add 1 monster with 1500 or less ATK from your Deck to your hand, but you cannot activate cards, or the effects of cards, with that name for the rest of this turn. You can only use this effect of 'Sangan' once per turn.",
		'Fiend/Effect', 1000, 600
	)
	, (
		'20663556', (select color_id from card_colors where card_color = 'Effect'), 'Substitoad', 'Water',
		"You can Tribute 1 monster to Special Summon 1 'Frog' monster from your Deck, except 'Frog the Jam'. 'Frog' monsters, except 'Frog the Jam', cannot be destroyed by battle.",
		'Aqua/Effect', 100, 2000
	)
	, (
		'00423585', (select color_id from card_colors where card_color = 'Effect'), 'Summoner Monk', 'Dark',
		"While this card is face-up on the field, it cannot be Tributed. If this card is Normal or Flip Summoned: Change this card to Defense Position. Once per turn: You can discard 1 Spell; Special Summon 1 Level 4 monster from your Deck, but that monster cannot attack this turn.",
		'Spellcaster/Effect', 800, 1600
	)
	, (
		'89399912', (select color_id from card_colors where card_color = 'Effect'), 'Tempest, Dragon Ruler of Storms', 'Wind',
		"If this card is in your hand or Graveyard: You can banish a total of 2 WIND and/or Dragon-Type monsters from your hand and/or Graveyard, except this card; Special Summon this card. During your opponent's End Phase, if this card was Special Summoned: Return it to the hand. You can discard this card and 1 WIND monster to the Graveyard; add 1 Dragon-Type monster from your Deck to your hand. If this card is banished: You can add 1 WIND Dragon-Type monster from your Deck to your hand. You can only use 1 'Tempest, Dragon Ruler of Storms' effect per turn, and only once that turn.",
		'Dragon/Effect', 2400, 2200
	)
	, (
		'88071625', (select color_id from card_colors where card_color = 'Effect'), 'The Tyrant Neptune', 'Water',
		"Cannot be Special Summoned. You can Tribute Summon this card by Tributing 1 monster. This card gains the total original ATK and DEF of the monster(s) Tributed for its Tribute Summon. When this card is Tribute Summoned: Target 1 Effect Monster in the Graveyard that was Tributed for the Tribute Summon; this card's name becomes that target's name, and this card gains that target's effects.",
		'Reptile/Effect', 0, 0
	)
	, (
		'71564252', (select color_id from card_colors where card_color = 'Effect'), 'Thunder King Rai-Oh', 'Light',
		"Neither player can add cards from their Deck to their hand except by drawing them. During either player's turn, when your opponent would Special Summon exactly 1 monster: You can send this face-up card to the Graveyard; negate the Special Summon, and if you do, destroy it.",
		'Thunder/Effect', 1900, 800
	)
	, (
		'26400609', (select color_id from card_colors where card_color = 'Effect'), 'Tidal, Dragon Ruler of Waterfalls', 'Water',
		"If this card is in your hand or Graveyard: You can banish a total of 2 WATER and/or Dragon-Type monsters from your hand and/or Graveyard, except this card; Special Summon this card. During your opponent's End Phase, if this card was Special Summoned: Return it to the hand. You can discard this card and 1 WATER monster to the Graveyard; send 1 monster from your Deck to the Graveyard. If this card is banished: You can add 1 WATER Dragon-Type monster from your Deck to your hand. You can only use 1 'Tidal, Dragon Ruler of Waterfalls' effect per turn, and only once that turn.",
		'Dragon/Effect', 2600, 2000
	)
	, (
		'98777036', (select color_id from card_colors where card_color = 'Effect'), 'Tragoedia', 'Dark',
		"When you take battle damage: You can Special Summon this card from your hand. Gains 600 ATK/DEF for each card in your hand. Once per turn: You can send 1 monster from your hand to the GY, then target 1 face-up monster your opponent controls with the same Level the sent monster had in the hand; take control of that face-up monster. Once per turn: You can target 1 monster in your GY; this card's Level becomes the same as that target's, until the end of this turn.",
		'Fiend/Effect', null, null
	)
	, (
		'33184167', (select color_id from card_colors where card_color = 'Effect'), 'Tribe-Infecting Virus', 'Water',
		"Discard 1 card from your hand and declare 1 Type of monster. Destroy all face-up monsters of the declared Type on the field.",
		'Aqua/Effect', 1600, 1000
	)
	, (
		'30539496', (select color_id from card_colors where card_color = 'Effect'), 'True King Lithosagym, the Disaster', 'Earth',
		"If this card is in your hand: You can destroy 2 other monsters in your hand and/or face-up on your field, including an EARTH monster, and if you do, Special Summon this card, and if you do that, and both destroyed monsters were EARTH, you can also look at your opponent's Extra Deck and banish up to 3 monsters from it with different names. If this card is destroyed by card effect: You can Special Summon 1 non-EARTH Wyrm-Type monster from your Graveyard. You can only use each effect of 'True King Lithosagym, the Disaster' once per turn.",
		'Wyrm/Effect', 2500, 2300
	)
	, (
		'44910027', (select color_id from card_colors where card_color = 'Effect'), 'Victory Dragon', 'Dark',
		"This card cannot be Special Summoned. To Tribute Summon this card, you must Tribute 3 Dragon-Type monsters. If this card attacks your opponent directly and reduces their Life Points to 0, you win the Match.",
		'Dragon/Effect', 2400, 3000
	)
	, (
		'59297550', (select color_id from card_colors where card_color = 'Effect'), 'Wind-Up Magician', 'Fire',
		"If the effect of a 'Wind-Up' monster is activated, except 'Wind-Up Magician': You can Special Summon 1 Level 4 or lower 'Wind-Up' monster from your Deck in face-up Defense Position. This effect can only be used once while this card is face-up on the field.",
		'Spellcaster/Effect', 600, 1800
	)
	, (
		'78010363', (select color_id from card_colors where card_color = 'Effect'), 'Witch of the Black Forest', 'Dark',
		"If this card is sent from the field to the GY: Add 1 monster with 1500 or less DEF from your Deck to your hand, but you cannot activate cards, or the effects of cards, with that name for the rest of this turn. You can only use this effect of 'Witch of the Black Forest' once per turn.",
		'Spellcaster/Effect', 1100, 1200
	)
	, (
		'03078576', (select color_id from card_colors where card_color = 'Effect'), 'Yata-Garasu', 'Wind',
		"This card cannot be Special Summoned. This card returns to its owner's hand during the End Phase of the turn it is Normal Summoned or flipped face-up. When this card inflicts Battle Damage to your opponent, they skip their next Draw Phase.",
		'Fiend/Spirit/Effect', 200, 100
	)
	, (
		'09411399', (select color_id from card_colors where card_color = 'Effect'), 'Destiny HERO - Malicious', 'Dark',
		"You can banish this card from your GY; Special Summon 1 'Destiny HERO - Malicious' from your Deck.",
		'Warrior/Effect', 800, 800
	)
	, (
		'28297833', (select color_id from card_colors where card_color = 'Effect'), 'Necroface', 'Dark',
		"If this card is Normal Summoned: Shuffle all banished cards into the Deck. This card gains 100 ATK for each card shuffled into the Main Deck by this effect. If this card is banished: Each player banishes 5 cards from the top of their Deck (or their entire Deck, if less than 5).",
		'Zombie/Effect', 1200, 1800
	)
	, (
		'28985331', (select color_id from card_colors where card_color = 'Effect'), 'Armageddon Knight', 'Dark',
		"When this card is Summoned: You can send 1 DARK monster from your Deck to the GY.",
		'Warrior/Effect', 1400, 1200
	)
	, (
		'82301904', (select color_id from card_colors where card_color = 'Effect'), 'Chaos Emperor Dragon - Envoy of the End', 'Dark',
		"Cannot be Normal Summoned/Set. Must be Special Summoned (from your hand) by banishing 1 LIGHT and 1 DARK monster from your GY. You can pay 1000 Life Points; send as many cards in both players' hands and on the field as possible to the GY, then inflict 300 damage to your opponent for each card sent to the opponent's GY by this effect. You cannot activate other cards or effects during the turn you activate this card's effect.",
		'Dragon/Effect', 3000, 2500
	)
	, (
		'57143342', (select color_id from card_colors where card_color = 'Effect'), 'Cir, Malebranche of the Burning Abyss', 'Dark',
		"If you control a monster that is not a 'Burning Abyss' monster, destroy this card. You can only use 1 of these effects of 'Cir, Malebranche of the Burning Abyss' per turn, and only once that turn.
&bull; If you control no Spell/Trap Cards: You can Special Summon this card from your hand.
&bull; If this card is sent to the Graveyard: You can target 1 'Burning Abyss' monster in your Graveyard, except 'Cir, Malebranche of the Burning Abyss'; Special Summon it.",
		'Fiend/Effect', 1600, 1200
	)
	, (
		'69015963', (select color_id from card_colors where card_color = 'Effect'), 'Cyber-Stein', 'Dark',
		"Pay 5000 Life Points. Special Summon 1 Fusion Monster from your Extra Deck to the field in Attack Position.",
		'Machine/Effect', 700, 500
	)
	, (
		'65192027', (select color_id from card_colors where card_color = 'Effect'), 'Dark Armed Dragon', 'Dark',
		"Cannot be Normal Summoned/Set. Must be Special Summoned (from your hand) by having exactly 3 DARK monsters in your GY. You can banish 1 DARK monster from your GY, then target 1 card on the field; destroy that target.",
		'Dragon/Effect', 2800, 1000
	)
	, (
		'14536035', (select color_id from card_colors where card_color = 'Effect'), 'Dark Grepher', 'Dark',
		"You can Special Summon this card (from your hand) by discarding 1 Level 5 or higher DARK monster. Once per turn: You can discard 1 DARK monster; send 1 DARK monster from your Deck to the GY.",
		'Warrior/Effect', 1700, 1600
	)
	, (
		'78868119', (select color_id from card_colors where card_color = 'Effect'), 'Deep Sea Diva', 'Water',
		"When this card is Normal Summoned: You can Special Summon 1 Level 3 or lower Sea Serpent-Type monster from your Deck.",
		'Sea Serpent/Effect', 200, 400
	)
	, (
		'58984738', (select color_id from card_colors where card_color = 'Effect'), 'Dinomight Knight, the True Dracofighter', 'Water',
		"To Tribute Summon this card face-up, you can Tribute a Continuous Spell/Trap Card you control, instead of a monster. Once per turn, during either player's turn, when your opponent activates a card or effect while you control this Tribute Summoned monster: You can take 1 'True Draco' or 'True King' Continuous Trap Card from your Deck, and either activate it or add it to your hand.",
		'Wyrm/Effect', 2500, 1200
	)
	, (
		'82385847', (select color_id from card_colors where card_color = 'Effect'), 'Dinowrestler Pankratops', 'Earth',
		"If your opponent controls more monsters than you do, you can Special Summon this card (from your hand). You can only Special Summon 'Dinowrestler Pankratops' once per turn this way. (Quick Effect): You can Tribute 1 'Dinowrestler' monster, then target 1 card your opponent controls; destroy it. You can only use this effect of 'Dinowrestler Pankratops' once per turn.",
		'Dinosaur/Effect', 2600, 0
	)
	, (
		'96570609', (select color_id from card_colors where card_color = 'Effect'), 'Ehther the Heavenly Monarch', 'Light',
		"You can Tribute Summon this card by Tributing 1 Tribute Summoned monster. If this card is Tribute Summoned: You can send 2 'Monarch' Spell/Trap Cards with different names from your hand and/or Deck to the Graveyard, and if you do, Special Summon 1 monster with 2400 or more ATK and 1000 DEF from your Deck, but return it to the hand during the End Phase. During your opponent's Main Phase, if this card is in your hand: You can banish 1 'Monarch' Spell/Trap Card from your Graveyard; immediately after this effect resolves, Tribute Summon this card (this is a Quick Effect).",
		'Fairy/Effect', 2800, 1000
	)
	, (
		'40044918', (select color_id from card_colors where card_color = 'Effect'), 'Elemental HERO Stratos', 'Wind',
		"When this card is Normal or Special Summoned: You can activate 1 of these effects.
&bull; You can destroy Spell/Trap Cards on the field, up to the number of 'HERO' monsters you control, except this card.
&bull; Add 1 'HERO' monster from your Deck to your hand.",
		'Warrior/Effect', 1800, 300
	)
	, (
		'33396948', (select color_id from card_colors where card_color = 'Effect'), 'Exodia the Forbidden One', 'Dark',
		"If you have 'Right Leg of the Forbidden One', 'Left Leg of the Forbidden One', 'Right Arm of the Forbidden One' and 'Left Arm of the Forbidden One' in addition to this card in your hand, you win the Duel.",
		'Spellcaster/Effect', 1000, 1000
	)
	, (
		'64034255', (select color_id from card_colors where card_color = 'Effect'), 'Genex Ally Birdman', 'Dark',
		"You can return 1 face-up monster you control to the hand; Special Summon this card from your hand, but banish it when it leaves the field, also it gains 500 ATK if the returned monster was WIND on the field.",
		'Machine/Effect', 1400, 400
	)
	, (
		'20758643', (select color_id from card_colors where card_color = 'Effect'), 'Graff, Malebranche of the Burning Abyss', 'Dark',
		"If you control a monster that is not a 'Burning Abyss' monster, destroy this card. You can only use 1 of these effects of 'Graff, Malebranche of the Burning Abyss' per turn, and only once that turn.
&bull; If you control no Spell/Trap Cards: You can Special Summon this card from your hand.
&bull; If this card is sent to the Graveyard: You can Special Summon 1 'Burning Abyss' monster from your Deck, except 'Graff, Malebranche of the Burning Abyss'.",
		'Fiend/Effect', 1000, 1500
	)
	, (
		'99177923', (select color_id from card_colors where card_color = 'Effect'), 'Infernity Archfiend', 'Dark',
		"When you draw this card, if you have no other cards in your hand: You can reveal this card; Special Summon this card from your hand. When this card is Special Summoned: You can add 1 'Infernity' card from your Deck to your hand. You must have no cards in your hand to activate and to resolve this effect.",
		'Fiend/Effect', 1800, 1200
	)
	, (
		'68184115', (select color_id from card_colors where card_color = 'Effect'), 'Inzektor Dragonfly', 'Dark',
		"Once per turn: You can equip 1 'Inzektor' monster from your hand or Graveyard to this card. If an Equip Card(s) is sent to your Graveyard while equipped to this card (except during the Damage Step): You can Special Summon 1 'Inzektor' monster from your Deck, except 'Inzektor Dragonfly'. While this card is equipped to a monster, that monster's Level is increased by 3.",
		'Insect/Effect', 1000, 1800
	)
	, (
		'16188701', (select color_id from card_colors where card_color = 'Effect'), 'Lady Debug', 'Light',
		"If this card is Normal or Special Summoned: You can add 1 Level 3 or lower Cyberse monster from your Deck to your hand. You can only use this effect of 'Lady Debug' once per turn.",
		'Cyberse/Effect', 1700, 1400
	)
	, (
		'38572779', (select color_id from card_colors where card_color = 'Effect'), 'Miscellaneousaurus', 'Fire',
		"During either player's Main Phase: You can send this card from your hand to the Graveyard; during this Main Phase, Dinosaur-Type monsters you control are unaffected by your opponent's activated effects. You can banish any number of Dinosaur-Type monsters from your Graveyard, including this card; Special Summon 1 Dinosaur-Type monster from your Deck with a Level equal to the total number of monsters banished to activate this effect, but destroy it during the End Phase. You can only use this effect of 'Miscellaneousaurus' once per turn.",
		'Dinosaur/Effect', 1800, 1000
	)
	, (
		'33508719', (select color_id from card_colors where card_color = 'Effect'), 'Morphing Jar', 'Earth',
		"FLIP: Both players discard their entire hands, then draw 5 cards.",
		'Rock/Flip/Effect', 700, 600
	)
	, (
		'16226786', (select color_id from card_colors where card_color = 'Effect'), 'Night Assailant', 'Dark',
		"FLIP: Target 1 monster your opponent controls; destroy that target.
When this card is sent from the hand to the Graveyard: Target 1 Flip Effect Monster in your Graveyard, except this card; return that target to the hand.",
		'Fiend/Flip/Effect', 200, 500
	)
	, (
		'68819554', (select color_id from card_colors where card_color = 'Effect'), 'Performage Damage Juggler', 'Light',
		"When a card or effect is activated that would inflict damage to you (Quick Effect): You can discard this card; negate the activation, and if you do, destroy that card. During the Battle Phase (Quick Effect): You can discard this card; reduce the next battle damage you would take this turn to 0. You can banish this card from your GY; add 1 'Performage' monster from your Deck to your hand, except 'Performage Damage Juggler'. You can only use this effect of 'Performage Damage Juggler' once per turn.",
		'Spellcaster/Effect', 1500, 1000
	)
	, (
		'12958919', (select color_id from card_colors where card_color = 'Effect'), 'Phantom Skyblaster', 'Dark',
		"When this card is Normal or Flip Summoned: You can Special Summon any number of 'Skyblaster Tokens' (Fiend/DARK/Level 4/ATK 500/DEF 500), up to the number of monsters you control. Once per turn, during your Standby Phase: You can inflict 300 damage to your opponent for each 'Skyblaster' monster you control. 'Skyblaster' monsters you control cannot declare an attack during the turn you activate this effect.",
		'Fiend/Effect', 1100, 800
	)
	, (
		'88264978', (select color_id from card_colors where card_color = 'Effect'), 'Red-Eyes Darkness Metal Dragon', 'Dark',
		"You can Special Summon this card (from your hand) by banishing 1 face-up Dragon-Type monster you control. Once per turn: You can Special Summon 1 Dragon-Type monster from your hand or Graveyard, except 'Red-Eyes Darkness Metal Dragon'.",
		'Dragon/Effect', 2800, 2400
	)
	, (
		'90307777', (select color_id from card_colors where card_color = 'Effect'), 'Shurit, Strategist of the Nekrozface', 'Water',
		"If you Ritual Summon exactly 1 'Nekroz' Ritual Monster with a card effect that requires use of monsters, this card can be used as the entire requirement. If this card is Tributed by a card effect: You can add 1 Warrior 'Nekroz' Ritual Monster from your Deck to your hand. You can only use this effect of 'Shurit, Strategist of the Nekroz' once per turn.",
		'Warrior/Effect', 300, 1800
	)
	, (
		'81275020', (select color_id from card_colors where card_color = 'Effect'), 'Speedroid Terrortop', 'Wind',
		"If you control no monsters, you can Special Summon this card (from your hand). When this card is Normal or Special Summoned: You can add 1 'Speedroid' monster from your Deck to your hand, except 'Speedroid Terrortop'. You can only use this effect of 'Speedroid Terrortop' once per turn.",
		'Machine/Effect', 1200, 600
	)
	, (
		'04474060', (select color_id from card_colors where card_color = 'Effect'), 'SPYRAL GEAR - Drone', 'Wind',
		"If this card is Normal or Special Summoned: You can look at the top 3 cards of your opponent's Deck, and if you do, place them on top of their Deck in any order. During either player's turn: You can Tribute this card, then target 1 'SPYRAL' monster you control; it gains 500 ATK for each card your opponent controls. You can banish this card and 1 'SPYRAL' card from your Graveyard, then target 1 'SPYRAL Super Agent' in your Graveyard; add it to your hand.",
		'Machine/Effect', 100, 100
	)
	, (
		'78080961', (select color_id from card_colors where card_color = 'Effect'), 'SPYRAL Quik-Fix', 'Earth',
		"If this card is Normal or Special Summoned: You can add 1 'SPYRAL GEAR' card from your Deck to your hand. If this card is in your GY and you control 'SPYRAL Super Agent': You can discard 1 card; Special Summon this card, but banish it when it leaves the field.",
		'Machine/Effect', 500, 400
	)
	, (
		'10802915', (select color_id from card_colors where card_color = 'Effect'), 'Tour Guide From the Underworld', 'Dark',
		"When this card is Normal Summoned: You can Special Summon 1 Level 3 Fiend monster from your hand or Deck, but it has its effects negated, also it cannot be used as a Synchro Material.",
		'Warrior/Effect', 1000, 600
	)
	, (
		'78872731', (select color_id from card_colors where card_color = 'Effect'), 'Zoodiac Ratpier', 'Earth',
		"If this card is Normal Summoned: You can send 1 'Zoodiac' card from your Deck to the Graveyard. An Xyz Monster whose original Type is Beast-Warrior and has this card as Xyz Material gains this effect.
&bull; Once per turn: You can detach 1 Xyz Material from this card; Special Summon 1 'Zoodiac Ratpier' from your hand or Deck.",
		'Beast-Warrior/Effect', 0, 0
	)
	, (
		'51858306', (select color_id from card_colors where card_color = 'Effect'), 'Eclipse Wyvern', 'Light',
		"If this card is sent to the Graveyard: Banish 1 Level 7 or higher LIGHT or DARK Dragon-Type monster from your Deck. If this card is banished from your Graveyard: You can add the monster banished by this card's effect to your hand.",
		'Dragon/Effect', 1600, 1000
	)
	, (
		'42790071', (select color_id from card_colors where card_color = 'Effect'), 'Altergeist Multifaker', 'Dark',
		"If you activate a Trap Card (except during the Damage Step): You can Special Summon this card from your hand. If this card is Special Summoned: You can Special Summon 1 'Altergeist' monster from your Deck in Defense Position, except 'Altergeist Multifaker'. You cannot Special Summon monsters the turn you activate this effect, except 'Altergeist' monsters. You can only use each effect of 'Altergeist Multifaker' once per turn.",
		'Spellcaster/Effect', 1200, 800
	)
	, (
		'61901281', (select color_id from card_colors where card_color = 'Effect'), 'Black Dragon Collapserpent', 'Dark',
		"Cannot be Normal Summoned/Set. Must be Special Summoned (from your hand) by banishing 1 LIGHT monster from your Graveyard, and cannot be Special Summoned by other ways. You can only Special Summon 'Black Dragon Collapserpent' once per turn this way. If this card is sent from the field to the Graveyard: You can add 1 'White Dragon Wyverburster' from your Deck to your hand.",
		'Dragon/Effect', 1800, 1700
	)
	, (
		'26889158', (select color_id from card_colors where card_color = 'Effect'), 'Salamangreat Gazelle', 'Fire',
		"If a 'Salamangreat' monster is sent to your GY, except 'Salamangreat Gazelle' (except during the Damage Step): You can Special Summon this card from your hand. If this card is Normal or Special Summoned: You can send 1 'Salamangreat' card from your Deck to the GY, except 'Salamangreat Gazelle'. You can only use each effect of 'Salamangreat Gazelle' once per turn.",
		'Cyberse/Effect', 1500, 1000
	)
	, (
		'99234526', (select color_id from card_colors where card_color = 'Effect'), 'White Dragon Wyverburster', 'Light',
		"Cannot be Normal Summoned/Set. Must be Special Summoned (from your hand) by banishing 1 DARK monster from your Graveyard, and cannot be Special Summoned by other ways. You can only Special Summon 'White Dragon Wyverburster' once per turn this way. If this card is sent from the field to the Graveyard: You can add 1 'Black Dragon Collapserpent' from your Deck to your hand.",
		'Dragon/Effect', 1700, 1800
	)
	, (
		'70711847', (select color_id from card_colors where card_color = 'Effect'), 'Danger! Nessie!', 'Dark',
		"You can reveal this card in your hand; your opponent randomly chooses 1 card from your entire hand, then you discard the chosen card. Then, if the discarded card was not 'Danger! Nessie!', Special Summon 1 'Danger! Nessie!' from your hand, and if you do, draw 1 card. If this card is discarded: You can add 1 'Danger!' card from your Deck to your hand, except 'Danger! Nessie!'. You can only use this effect of 'Danger! Nessie!' once per turn.",
		'Aqua/Effect', 1600, 2800
	)
	, (
		'43694650', (select color_id from card_colors where card_color = 'Effect'), 'Danger!? Jackalope?', 'Dark',
		"You can reveal this card in your hand; your opponent randomly chooses 1 card from your entire hand, then you discard the chosen card. Then, if the discarded card was not 'Danger!? Jackalope?', Special Summon 1 'Danger!? Jackalope?' from your hand, and if you do, draw 1 card. If this card is discarded: You can Special Summon 1 'Danger!' monster from your Deck in Defense Position, except 'Danger!? Jackalope?'. You can only use this effect of 'Danger!? Jackalope?' once per turn.",
		'Beast/Effect', 500, 2000
	)
	, (
		'99745551', (select color_id from card_colors where card_color = 'Effect'), 'Danger!? Tsuchinoko?', 'Dark',
		"You can reveal this card in your hand; your opponent randomly chooses 1 card from your entire hand, then you discard the chosen card. Then, if the discarded card was not 'Danger!? Tsuchinoko?', Special Summon 1 'Danger!? Tsuchinoko?' from your hand, and if you do, draw 1 card. If this card is discarded: You can Special Summon this card. You can only use this effect of 'Danger!? Tsuchinoko?' once per turn.",
		'Reptile/Effect', 1300, 0
	)
	, (
		'69207766', (select color_id from card_colors where card_color = 'Effect'), 'Inzektor Hornet', 'Dark',
		"Once per turn: You can equip 1 'Inzektor' monster from your hand or Graveyard to this card. While this card is equipped to a monster, that monster's Level is increased by 3, also it gains ATK and DEF equal to this card's ATK and DEF. While this card is equipped to a monster: You can send this Equip Card to the Graveyard to target 1 card on the field; destroy that target.",
		'Insect/Effect', 500, 200
	);