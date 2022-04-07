Feature: pay in four loan simulation
  The loan application will be able to publish a new customer interested in loan event.

  Scenario: A new customer requesting for a loan will be published as kafka event.
    Given A loan simulation request is submitted by a newUser
      | first_name | last_name | loan_amount | loan_requested_date |
      | Jhon       | Doe       | 345.43      | 2022-04-03          |
      | Anna       | Evanova   | 545.12      | 2022-02-23          |
      | Julio      | Garcia    | 297.73      | 2022-01-13          |
    When The requesting customer is a new customer a kafka event will be published
    Then The new customer interested in loan event will be processed and saved to data base
