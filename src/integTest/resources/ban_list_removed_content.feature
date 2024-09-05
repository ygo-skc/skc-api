@BanList
Feature: Fetch cards that no longer have a status in the requested ban list, IE a card is no longer forbidden/limited/semi-limited.

  Scenario Outline: User requests removed cards for a given ban list.
    When user requests removed content for ban list whose effective date is "<listRequested>"
    Then HTTP status of response should be 200
    And requested list in response matches the date used in request: "<listRequested>"
    And previous ban list used to compare is "<comparedTo>"
    And total removed cards is <numRemoved>

    Examples:
      | listRequested | comparedTo | numRemoved |
      | 2020-04-01    | 2020-01-20 | 4          |
      | 2020-01-20    | 2019-10-14 | 10         |
      | 2019-10-14    | 2019-07-15 | 9          |
      | 2019-07-15    | 2019-04-29 | 8          |
      | 2019-04-29    | 2019-01-28 | 8          |
      | 2019-01-28    | 2018-12-03 | 9          |
      | 2015-11-09    |            | 0          |