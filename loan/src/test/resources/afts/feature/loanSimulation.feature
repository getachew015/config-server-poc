Feature: pay in four loan simulation
  This allows a customer to simulate a loan and repayment plan in a four interest free payments.

  Scenario Outline: Simulate a loan repayment plan for a qualified loan amount.
    Given A customer requests loan simulation for amount <loan-amount>
    When The loan-amount <loan-amount> is less than max-loan amount
    Then The customer with customerId <customerId> will get installment plan with four min installment amounts of <min-payment>
    Examples:
      | customerId | loan-amount | min-payment |
      | 'cust1001' | 159.73      | 39.93       |
      | 'cust1002' | 553.73      | 138.43      |
      | 'cust1003' | 479.99      | 119.99      |

  Scenario: Loan simulation request for un qualified loan amount.
    When A customer "cust1001" requests for a loan simulation
    And The loan amount 743.99 is more than the maximum loan amount allowed
    Then I will get '400 BAD_REQUEST "Loan Amount Exceeded Max Threshold!"' BusinessException
