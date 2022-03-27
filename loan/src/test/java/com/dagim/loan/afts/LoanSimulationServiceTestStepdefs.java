package com.dagim.loan.afts;

import com.dagim.loan.api.LoanApi;
import com.dagim.loan.configuration.LoanConfigDetail;
import com.dagim.loan.exception.BusinessException;
import com.dagim.loan.model.Loan;
import com.dagim.loan.model.LoanRepaymentPlan;
import com.dagim.loan.service.LoanSimulationService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
@Cucumber
@Slf4j
public class LoanSimulationServiceTestStepdefs {

  @Autowired private LoanSimulationService simulationService;
  @Autowired private LoanConfigDetail loanConfigDetail;
  @Autowired private LoanApi loanApi;
  private String unQualifiedCustomerId;
  private double unQualifiedLoan;

  @Given("A customer requests loan simulation for amount {double}")
  public void aCustomerRequestsLoanSimulationByCustomerIdAndLoanAmount(Double loanAmount) {
    // asseting customer has requested loan
    assertThat(loanAmount).isNotNull();
  }

  @When("The loan-amount {double} is less than max-loan amount")
  public void theLoanAmountIsLessThanMaxLoanAmount(double loanAmount) {
    // asserting requested loan amount is within acceptable range
    assertThat(loanAmount).isLessThan(loanConfigDetail.getMaxAmount());
  }

  @Then(
      "The customer customerId {string} will get installment plan with four min installment amount {double} amounts")
  public void iWillGetInstallmentPlanWithMinPaymentAmounts(String customerId, double loanAmount) {
    log.info("customerId ... {} and loan ... {}", customerId, loanAmount);
    Loan simulatedLoan = simulationService.simulateLoanPaymentPlan(customerId, loanAmount);
    Set<LoanRepaymentPlan> repaymentPlanHashSet = simulatedLoan.getRepaymentPlan();
    assertThat(repaymentPlanHashSet.stream().findAny().get().getLoanAmount())
        .isEqualTo(loanAmount / 4);
  }

  @When("A customer {string} requests for a loan simulation")
  public void aCustomerCustRequestsForALoanSimulation(String customerId) {
    this.unQualifiedCustomerId = customerId;
  }

  @And("The loan amount {double} is more than the maximum loan amount allowed")
  public void theLoanAmountIsMoreThanTheMaximumLoanAmountAllowed(double loanAmount) {
    unQualifiedLoan = loanAmount;
    assertThat(loanAmount).isGreaterThan(loanConfigDetail.getMaxAmount());
  }

  @Then("I will get {string} BusinessException")
  public void iWillGetLoanAmountExceededMaxThresholdBusinessException(String businessException) {
    BusinessException thrownException;
    try {
      thrownException =
          assertThrows(
              BusinessException.class,
              () -> {
                loanApi.simulateLoanRepaymentPlan(unQualifiedCustomerId, unQualifiedLoan);
              });
      assertThat(businessException)
          .isEqualTo(thrownException.getMessage());
    } catch (BusinessException exception) {
      log.error("cucumber AFT test thrown ... ", exception);
    }
  }
}
