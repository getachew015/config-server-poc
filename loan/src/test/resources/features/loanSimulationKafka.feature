Feature: pay in four loan simulation
  The loan application will be able to publish a new customer interested in loan event.

  Scenario: A new customer requesting for a loan will be published as kafka event.
    Given A loan simulation request is submitted
    When The requesting customer is a new customer a kafka event will be published
    Then The new customer interested in loan event will be processed and saved to data base
