Feature: Fetch information about an individual card. Use Card endpoints of SKC API to fetch info about a card.

    Scenario Outline: Fetching all information about a card and checking if response has the necessary attributes and values (see other scenarios for more specific value checks).
        Given I request all info for card with ID: "<cardID>"
        Then http status of response should be <httpStatus>
        And cardName should be "<cardName>"
        And cardID should be "<cardID>"
        And cardColor should be "<cardColor>"
        And cardAttribute should be "<cardAttribute>"
        And monsterType should be "<monsterType>"
        And monsterAtk should be "<monsterAtk>"
        And monsterDef should be "<monsterDef>"
        And level should be "<level>"
        And rank should be "<rank>"
        And card effect should not be empty or null
        And products card is found in should be greater than or equal to <productsFoundIn>
        And ban lists card is found in should be greater than or equal to <restrictedIn>

        Examples:
            | cardID   | cardName                                | cardColor        | cardAttribute | monsterType                     | monsterAtk | monsterDef | level | rank | httpStatus | productsFoundIn  | restrictedIn     |
            | 21844576 | Elemental HERO Avian                    | Normal           | Wind          | Warrior/Normal                  | 1000       | 1000       | 3     |      | 200        | 5                | 0                |
            | 40044918 | Elemental HERO Stratos                  | Effect           | Wind          | Warrior/Effect                  | 1800       | 300        | 4     |      | 200        | 4                | 14               |
            | 35809262 | Elemental HERO Flame Wingman            | Fusion           | Wind          | Warrior/Fusion/Effect           | 2100       | 1200       | 6     |      | 200        | 3                | 0                |
            | 47963370 | Magician of Chaos                       | Ritual           | Dark          | Spellcaster/Ritual/Effect       | 2500       | 2100       | 7     |      | 200        | 1                | 0                |
            | 29981921 | Legendary Six Samurai - Shi En          | Synchro          | Dark          | Warrior/Synchro/Effect          | 2500       | 1400       | 5     |      | 200        | 2                | 0                |
            | 84013237 | Number 39: Utopia                       | Xyz              | Light         | Warrior/Xyz/Effect              | 2500       | 2000       |       | 4    | 200        | 3                | 0                |
            | 51531505 | Dragonpit Magician                      | Pendulum-Normal  | Water         | Spellcaster/Pendulum            | 900        | 2700       | 7     |      | 200        | 1                | 0                |
            | 15308295 | Abyss Actor - Comic Relief              | Pendulum-Effect  | Dark          | Fiend/Pendulum/Effect           | 1000       | 2000       | 3     |      | 200        | 2                | 0                |
            | 84569886 | D/D/D Super Doom King Purple Armageddon | Pendulum-Fusion  | Dark          | Fiend/Fusion/Pendulum/Effect    | 3500       | 3000       | 10    |      | 200        | 2                | 0                |
            | 90036274 | Clear Wing Fast Dragon                  | Pendulum-Synchro | Wind          | Dragon/Synchro/Pendulum/Effect  | 2500       | 2000       | 7     |      | 200        | 1                | 0                |
            | 67865534 | Magician of Hope                        | Pendulum-Xyz     | Light         | Spellcaster/Xyz/Pendulum/Effect | 2500       | 2000       |       | 4    | 200        | 1                | 0                |
            | 10547580 | Ancient Gear Ballista                   | Link             | Earth         | Machine/Link/Effect             | 1500       |            |       |      | 200        | 1                | 0                |
            | 08949584 | A Hero Lives                            | Spell            | Spell         |                                 |            |            |       |      | 200        | 2                | 11               |
            | 74414885 | NEXT                                    | Trap             | Trap          |                                 |            |            |       |      | 200        | 1                | 0                |


    Scenario Outline: Fetching information (without all info flag) about a card and checking if response has the necessary attributes and values (see other scenarios for more specific value checks).
        Given I request info without all info flag for card with ID: "<cardID>"
        Then http status of response should be <httpStatus>
        And cardName should be "<cardName>"
        And cardID should be "<cardID>"
        And cardColor should be "<cardColor>"
        And cardAttribute should be "<cardAttribute>"
        And monsterType should be "<monsterType>"
        And monsterAtk should be "<monsterAtk>"
        And monsterDef should be "<monsterDef>"
        And card effect should not be empty or null
        And products card is found in should not be included

        Examples:
            | cardID   | cardName                                | cardColor        | cardAttribute | monsterType                     | monsterAtk | monsterDef | httpStatus |
            | 21844576 | Elemental HERO Avian                    | Normal           | Wind          | Warrior/Normal                  | 1000       | 1000       | 200        |
            | 40044918 | Elemental HERO Stratos                  | Effect           | Wind          | Warrior/Effect                  | 1800       | 300        | 200        |
            | 35809262 | Elemental HERO Flame Wingman            | Fusion           | Wind          | Warrior/Fusion/Effect           | 2100       | 1200       | 200        |
            | 47963370 | Magician of Chaos                       | Ritual           | Dark          | Spellcaster/Ritual/Effect       | 2500       | 2100       | 200        |
            | 29981921 | Legendary Six Samurai - Shi En          | Synchro          | Dark          | Warrior/Synchro/Effect          | 2500       | 1400       | 200        |
            | 84013237 | Number 39: Utopia                       | Xyz              | Light         | Warrior/Xyz/Effect              | 2500       | 2000       | 200        |
            | 51531505 | Dragonpit Magician                      | Pendulum-Normal  | Water         | Spellcaster/Pendulum            | 900        | 2700       | 200        |
            | 15308295 | Abyss Actor - Comic Relief              | Pendulum-Effect  | Dark          | Fiend/Pendulum/Effect           | 1000       | 2000       | 200        |
            | 84569886 | D/D/D Super Doom King Purple Armageddon | Pendulum-Fusion  | Dark          | Fiend/Fusion/Pendulum/Effect    | 3500       | 3000       | 200        |
            | 67865534 | Magician of Hope                        | Pendulum-Xyz     | Light         | Spellcaster/Xyz/Pendulum/Effect | 2500       | 2000       | 200        |
            | 90036274 | Clear Wing Fast Dragon                  | Pendulum-Synchro | Wind          | Dragon/Synchro/Pendulum/Effect  | 2500       | 2000       | 200        |
            | 10547580 | Ancient Gear Ballista                   | Link             | Earth         | Machine/Link/Effect             | 1500       |            | 200        |
            | 08949584 | A Hero Lives                            | Spell            | Spell         |                                 |            |            | 200        |
            | 74414885 | NEXT                                    | Trap             | Trap          |                                 |            |            | 200        |