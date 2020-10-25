Feature: Fetch Card Info
    Use Card endpoints of YGO API to fetch info about a card.

    Scenario: Fetch info for a card
        Given I request info for card with ID: "<cardId>"
        Then http status of response should be <httpStatus>
        And cardName should be "<cardName>"
        And cardColor should be "<cardColor>"
        And cardAttribute should be "<cardAttribute>"
        And monsterType should be "<monsterType>"
        And monsterAtk should be "<monsterAtk>"
        And monsterDef should be "<monsterDef>"
    Examples:
        | cardId    | cardName                                      | cardColor                 | cardAttribute | monsterType                           | monsterAtk | monsterDef | httpStatus |
        | 21844576  | Elemental HERO Avian                          | Normal                    | Wind          | Warrior/Normal                        | 1000       | 1000       | 200        |
        | 40044918  | Elemental HERO Stratos                        | Effect                    | Wind          | Warrior/Effect                        | 1800       | 300        | 200        |
        | 35809262  | Elemental HERO Flame Wingman                  | Fusion                    | Wind          | Warrior/Fusion/Effect                 | 2100       | 1200       | 200        |
        | 47963370  | Magician of Chaos                             | Ritual                    | Dark          | Spellcaster/Ritual/Effect             | 2500       | 2100       | 200        |
        | 29981921  | Legendary Six Samurai - Shi En                | Synchro                   | Dark          | Warrior/Synchro/Effect                | 2500       | 1400       | 200        |
        | 84013237  | Number 39: Utopia                             | XYZ                       | Light         | Warrior/Xyz/Effect                    | 2500       | 2000       | 200        |
        | 51531505  | Dragonpit Magician                            | Pendulum-Normal           | Water         | Spellcaster/Pendulum                  | 900        | 2700       | 200        |
        | 15308295  | Abyss Actor - Comic Relief                    | Pendulum-Effect           | Dark          | Fiend/Pendulum/Effect                 | 1000       | 2000       | 200        |
        | 84569886  | D/D/D Super Doom King Purple Armageddon       | Pendulum-Fusion           | Dark          | Fiend/Fusion/Pendulum/Effect          | 3500       | 3000       | 200        |
        | 67865534  | Magician of Hope                              | Pendulum-Xyz              | Light         | Spellcaster/Xyz/Pendulum/Effect       | 2500       | 2000       | 200        |
        | 90036274  | Clear Wing Fast Dragon                        | Pendulum-Synchro          | Wind          | Dragon/Synchro/Pendulum/Effect        | 2500       | 2000       | 200        |
        | 10547580  | Ancient Gear Ballista                         | Link                      | Earth         | Machine/Link/Effect                   | 1500       |            | 200        |
        | 08949584  | A Hero Lives                                  | Spell                     | Spell         |                                       |            |            | 200        |
        | 74414885  | NEXT                                          | Trap                      | Trap          |                                       |            |            | 200        |