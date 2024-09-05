@BanList
Feature: Fetch start dates for ban lists stored in the DB.

  Scenario: Use ban list date endpoint to fetch start dates and verify they are correct.
    When User requests ban list dates
    Then HTTP status of response should be 200
    And First - oldest - ban list found in DB has effectiveDate of "2017-06-12" and newest ban list found in DB has effectiveDate of "2024-09-02"