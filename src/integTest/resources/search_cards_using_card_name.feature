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
    Given user searches using card name "<cardName>" and card ID "<cardId>"
    When user sends search request
    Then search results request should return with OK http status
    And number of search results should be 5

    Examples:
      | cardName  | cardId |
      | blue-eyes | 1139   |
      | blue eyes | 1139   |
      | faris     | 18     |
      | FARIS     | 18     |
      | fAris     | 18     |
      | Red-Eyes  | 746    |
      | Red Eyes  | 746    |