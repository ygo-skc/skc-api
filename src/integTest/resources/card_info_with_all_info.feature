@Card
Feature: Fetch information about an individual card. There are two flows within the same endpoint: users can either retrieve product/ban list info for a card (fetchAllInfo is true) or they can choose to not retrieve the info (fetchAllInfo is false).
    Scenario Outline: Fetching all information about a monster card and checking if response has the necessary attributes and values for product info. These cards have not been in any ban list so info for that card regarding ban lists will be null.
        Given I want all info for card with ID "<cardID>" - IE, info on ban lists, products, etc
        When I request info for the card
        Then http status of response should be <httpStatus>
        And card info return value should contain "<cardName>", "<cardID>", "<cardColor>", "<cardAttribute>"
        And monster card specific info return value be "<monsterType>" "<monsterAtk>" "<monsterDef>" "<level>" "<rank>"
        And card effect should not be empty or null
        And products card is found in should be greater than or equal to <productsFoundIn>
        And card will not be found in any ban list

        Examples:
            | cardID   | cardName                                | cardColor        | cardAttribute | monsterType                     | monsterAtk | monsterDef | level | rank | httpStatus | productsFoundIn  |
            | 21844576 | Elemental HERO Avian                    | Normal           | Wind          | Warrior/Normal                  | 1000       | 1000       | 3     |      | 200        | 5                |
            | 79979666 | Elemental HERO Bubbleman                | Effect           | Water         | Warrior/Effect                  | 800        | 1200       | 4     |      | 200        | 4                |
            | 35809262 | Elemental HERO Flame Wingman            | Fusion           | Wind          | Warrior/Fusion/Effect           | 2100       | 1200       | 6     |      | 200        | 3                |
            | 47963370 | Magician of Chaos                       | Ritual           | Dark          | Spellcaster/Ritual/Effect       | 2500       | 2100       | 7     |      | 200        | 1                |
            | 29981921 | Legendary Six Samurai - Shi En          | Synchro          | Dark          | Warrior/Synchro/Effect          | 2500       | 1400       | 5     |      | 200        | 2                |
            | 84013237 | Number 39: Utopia                       | Xyz              | Light         | Warrior/Xyz/Effect              | 2500       | 2000       |       | 4    | 200        | 3                |
            | 51531505 | Dragonpit Magician                      | Pendulum-Normal  | Water         | Spellcaster/Pendulum/Normal     | 900        | 2700       | 7     |      | 200        | 1                |
            | 15308295 | Abyss Actor - Comic Relief              | Pendulum-Effect  | Dark          | Fiend/Pendulum/Effect           | 1000       | 2000       | 3     |      | 200        | 2                |
            | 84569886 | D/D/D Super Doom King Purple Armageddon | Pendulum-Fusion  | Dark          | Fiend/Fusion/Pendulum/Effect    | 3500       | 3000       | 10    |      | 200        | 2                |
            | 90036274 | Clear Wing Fast Dragon                  | Pendulum-Synchro | Wind          | Dragon/Synchro/Pendulum/Effect  | 2500       | 2000       | 7     |      | 200        | 1                |
            | 67865534 | Magician of Hope                        | Pendulum-Xyz     | Light         | Spellcaster/Xyz/Pendulum/Effect | 2500       | 2000       |       | 4    | 200        | 1                |
            | 10547580 | Ancient Gear Ballista                   | Link             | Earth         | Machine/Link/Effect             | 1500       |            |       |      | 200        | 1                |



    Scenario Outline: Fetching all information about a monster card and checking if response has the necessary attributes and values for product info. These cards have not been in any ban list so info for that card regarding ban lists will be null.
        Given I want all info for card with ID "<cardID>" - IE, info on ban lists, products, etc
        When I request info for the card
        Then http status of response should be <httpStatus>
        And card info return value should contain "<cardName>", "<cardID>", "<cardColor>", "<cardAttribute>"
        And monster card specific info return value be "<monsterType>" "<monsterAtk>" "<monsterDef>" "<level>" "<rank>"
        And card effect should not be empty or null
        And products card is found in should be greater than or equal to <productsFoundIn>
        And ban lists card is found in should be greater than or equal to <restrictedIn>

        Examples:
            | cardID   | cardName                                | cardColor        | cardAttribute | monsterType                     | monsterAtk | monsterDef | level | rank | httpStatus | productsFoundIn  | restrictedIn     |
            | 07902349 | Left Arm of the Forbidden One           | Normal           | Dark          | Spellcaster/Normal              | 200        | 300        | 1     |      | 200        | 3                | 25               |
            | 40044918 | Elemental HERO Stratos                  | Effect           | Wind          | Warrior/Effect                  | 1800       | 300        | 4     |      | 200        | 4                | 10               |
            | 43387895 | Supreme King Dragon Starving Venom      | Fusion           | Dark          | Dragon/Fusion/Effect            | 2800       | 2000       | 8     |      | 200        | 1                | 17               |



    Scenario Outline: Fetching all information about a spell/trap card and checking if response has the necessary attributes and values for product info. These cards have not been in any ban list so info for that card regarding ban lists will be null.
        Given I want all info for card with ID "<cardID>" - IE, info on ban lists, products, etc
        When I request info for the card
        Then http status of response should be <httpStatus>
        And card info return value should contain "<cardName>", "<cardID>", "<cardColor>", "<cardAttribute>"
        And monsterType, monsterAtk, monsterDef, level, rank should all be null
        And card effect should not be empty or null
        And products card is found in should be greater than or equal to <productsFoundIn>
        And card will not be found in any ban list

        Examples:
            | cardID   | cardName                                | cardColor        | cardAttribute |  httpStatus | productsFoundIn  |
            | 74414885 | NEXT                                    | Trap             | Trap          | 200         | 1                |



    Scenario Outline: Fetching all information about a spell/trap card and checking if response has the necessary attributes and values for product info. These cards have been in a ban list so info on ban lists should be included.
        Given I want all info for card with ID "<cardID>" - IE, info on ban lists, products, etc
        When I request info for the card
        Then http status of response should be <httpStatus>
        And card info return value should contain "<cardName>", "<cardID>", "<cardColor>", "<cardAttribute>"
        And monsterType, monsterAtk, monsterDef, level, rank should all be null
        And card effect should not be empty or null
        And products card is found in should be greater than or equal to <productsFoundIn>
        And ban lists card is found in should be greater than or equal to <restrictedIn>

        Examples:
            | cardID   | cardName                                | cardColor        | cardAttribute |  httpStatus | productsFoundIn  | restrictedIn     |
            | 08949584 | A Hero Lives                            | Spell            | Spell         | 200         | 2                | 11               |
