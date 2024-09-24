@Card
Feature: Fetch information about an individual card. There are two flows within the same endpoint: users can either retrieve product/ban list info for a card (fetchAllInfo is true) or they can choose to not retrieve the info (fetchAllInfo is false).

  Scenario Outline: Fetching information (without all info flag) about a monster card and checking if response has the necessary attributes and values (see other scenarios for more specific value checks).
    Given I want basic info for card with ID "<cardID>" - IE, no info on ban lists, products, etc
    When I request info for the card
    Then http status of response should be <httpStatus>
    And card info return value should contain "<cardName>", "<cardID>", "<cardColor>", "<cardAttribute>"
    And monster card specific info return value be "<monsterType>" "<monsterAtk>" "<monsterDef>" "<level>" "<rank>"
    And card effect should not be empty or null
    And products card is found in should not be included
    And ban lists card is found in should not be included

    Examples:
      | cardID   | cardName                                | cardColor        | cardAttribute | monsterType                     | monsterAtk | monsterDef | level | rank | httpStatus |
      | 21844576 | Elemental HERO Avian                    | Normal           | Wind          | Warrior/Normal                  | 1000       | 1000       | 3     |      | 200        |
      | 79979666 | Elemental HERO Bubbleman                | Effect           | Water         | Warrior/Effect                  | 800        | 1200       | 4     |      | 200        |
      | 40044918 | Elemental HERO Stratos                  | Effect           | Wind          | Warrior/Effect                  | 1800       | 300        | 4     |      | 200        |
      | 35809262 | Elemental HERO Flame Wingman            | Fusion           | Wind          | Warrior/Fusion/Effect           | 2100       | 1200       | 6     |      | 200        |
      | 47963370 | Magician of Chaos                       | Ritual           | Dark          | Spellcaster/Ritual/Effect       | 2500       | 2100       | 7     |      | 200        |
      | 29981921 | Legendary Six Samurai - Shi En          | Synchro          | Dark          | Warrior/Synchro/Effect          | 2500       | 1400       | 5     |      | 200        |
      | 84013237 | Number 39: Utopia                       | Xyz              | Light         | Warrior/Xyz/Effect              | 2500       | 2000       |       | 4    | 200        |
      | 51531505 | Dragonpit Magician                      | Pendulum-Normal  | Water         | Spellcaster/Pendulum/Normal     | 900        | 2700       | 7     |      | 200        |
      | 15308295 | Abyss Actor - Comic Relief              | Pendulum-Effect  | Dark          | Fiend/Pendulum/Effect           | 1000       | 2000       | 3     |      | 200        |
      | 84569886 | D/D/D Super Doom King Purple Armageddon | Pendulum-Fusion  | Dark          | Fiend/Fusion/Pendulum/Effect    | 3500       | 3000       | 10    |      | 200        |
      | 90036274 | Clear Wing Fast Dragon                  | Pendulum-Synchro | Wind          | Dragon/Synchro/Pendulum/Effect  | 2500       | 2000       | 7     |      | 200        |
      | 67865534 | Magician of Hope                        | Pendulum-Xyz     | Light         | Spellcaster/Xyz/Pendulum/Effect | 2500       | 2000       |       | 4    | 200        |
      | 10547580 | Ancient Gear Ballista                   | Link             | Earth         | Machine/Link/Effect             | 1500       |            |       |      | 200        |


  Scenario Outline: Fetching information (without all info flag) about a spell/trap card and checking if response has the necessary attributes and values (see other scenarios for more specific value checks).
    Given I want basic info for card with ID "<cardID>" - IE, no info on ban lists, products, etc
    When I request info for the card
    Then http status of response should be <httpStatus>
    And card info return value should contain "<cardName>", "<cardID>", "<cardColor>", "<cardAttribute>"
    And monsterType, monsterAtk, monsterDef, level, rank should all be null
    And card effect should not be empty or null
    And products card is found in should not be included
    And ban lists card is found in should not be included

    Examples:
      | cardID   | cardName     | cardColor | cardAttribute | httpStatus |
      | 08949584 | A Hero Lives | Spell     | Spell         | 200        |
      | 74414885 | NEXT         | Trap      | Trap          | 200        |