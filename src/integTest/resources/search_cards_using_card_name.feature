@CardSearch
@Card
Feature: Search for specific cards using only the card name as search criteria.

  Scenario Outline: User searches using the full card name.
    Given user searches using card name "<cardName>"
    When user sends search request
    Then search results request should return with OK http status
    And number of search results should be <expectedNumResults>

    Examples:
      | cardName               | expectedNumResults |
      | blue-eyes white dragon | 3                  |
      | blue eyes white dragon | 3                  |
      | faris                  | 1                  |
      | FARIS                  | 1                  |
      | fAris                  | 1                  |
      | Red-Eyes Black Dragon  | 5                  |
      | Red Eyes Black Dragon  | 5                  |


  Scenario Outline: User searches using card name and uses other available filters to narrow down the card they want.
    Given user searches using card name "<cardName>", card ID "<cardId>", attribute "<attribute>", card color "<cardColor>", and monster type "<monsterType>"
    When user sends search request
    Then search results request should return with OK http status
    And number of search results should be 1

    Examples:
      | cardName  | cardId | attribute | cardColor | monsterType |
      | blue-eyes | 1139   | light     | normal    | dragon      |
      | blue eyes | 1139   | light     | normal    | dragon      |
      | faris     | 18     | dark      | effect    | war         |
      | FARIS     | 18     | da        | eff       | war         |
      | fAris     | 18     | dark      | effect    | warrior     |
      | Red-Eyes  | 746    | dark      | normal    | dragon      |
      | Red Eyes  | 746    | dark      | normal    | dragon      |