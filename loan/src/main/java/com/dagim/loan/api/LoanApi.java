package com.dagim.loan.api;

import com.dagim.loan.configuration.LoanConfigDetail;
import com.dagim.loan.exception.BusinessException;
import com.dagim.loan.repository.BankAccountsClient;
import com.dagim.loan.service.LoanSimulationService;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/loan/v1")
@Slf4j
public class LoanApi {

  @Autowired CircuitBreakerRegistry circuitBreakerRegistry;
  CircuitBreaker loanBreaker;
  @Autowired private BankAccountsClient client;
  @Autowired private LoanSimulationService loanSimulationService;
  @Autowired private LoanConfigDetail loanConfigDetail;

  @GetMapping(path = "/customer/{customerId}", produces = "application/json")
  @CircuitBreaker(name = "loanBreaker", fallbackMethod = "findCustomerServiceUnReachable")
  public ResponseEntity<?> findCustomerById(@PathVariable String customerId) {

    return new ResponseEntity<>(client.getCustomerAccounts(customerId), HttpStatus.OK);
  }

  @GetMapping(path = "/customer/payInFour", produces = "application/json")
  @CircuitBreaker(name = "loanBreaker", fallbackMethod = "findCustomerServiceUnReachable")
  public ResponseEntity<?> requestPayInFourLoan(
      @RequestHeader(required = true) String customerId,
      @RequestHeader(required = true) String firstName,
      @RequestHeader(required = true) String lastName,
      @RequestHeader(required = true) double loanAmount) {
    if (loanAmount < loanConfigDetail.getMaxAmount())
      return new ResponseEntity<>(
          loanSimulationService.requestPayInFourLoan(customerId, firstName, lastName, loanAmount),
          HttpStatus.OK);
    else throw new BusinessException(HttpStatus.BAD_REQUEST, "Loan Amount Exceeded Max Threshold!");
  }

  @GetMapping(path = "/customer", produces = "application/json")
  public ResponseEntity<?> simulateLoanRepaymentPlan(
      @RequestHeader(required = true) String customerId,
      @RequestHeader(required = true) double loanAmount) {
    if (loanAmount < loanConfigDetail.getMaxAmount())
      return new ResponseEntity<>(
          loanSimulationService.simulateLoanPaymentPlan(customerId, loanAmount), HttpStatus.OK);
    else throw new BusinessException(HttpStatus.BAD_REQUEST, "Loan Amount Exceeded Max Threshold!");
  }

  public ResponseEntity<?> findCustomerServiceUnReachable(Exception exception) {
    exception.printStackTrace();
    throw new BusinessException(
        HttpStatus.FAILED_DEPENDENCY, "Bank Accounts Service Unreachable !");
  }
}
