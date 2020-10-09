Feature: Fetch Card Info
    Use Card endpoints of YGO API to fetch info about a card.

    Scenario: Fetch info for a card
        Given I request info for card with ID: "<cardId>"
        Then cardName should be "<cardName>"
        Then cardColor should be "<cardColor>"
        Then cardAttribute should be "<cardAttribute>"
        Then monsterType should be "<monsterType>"
    Examples:
        | cardId    | cardName                          | cardColor     | cardAttribute | monsterType                       |
        | 21844576  | Elemental HERO Avian              | Normal        | Wind          | Warrior/Normal                    |
        | 40044918  | Elemental HERO Stratos            | Effect        | Wind          | Warrior/Effect                    |
        | 35809262  | Elemental HERO Flame Wingman      | Fusion        | Wind          | Warrior/Fusion/Effect             |
        | 47963370  | Magician of Chaos                 | Ritual        | Dark          | Spellcaster/Ritual/Effect         |
        | 29981921  | Legendary Six Samurai - Shi En    | Synchro       | Dark          | Warrior/Synchro/Effect            |