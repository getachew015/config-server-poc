package com.dagim.loan.api;


import com.dagim.loan.exception.BusinessException;
import com.dagim.loan.repository.BankAccountsClient;
import com.dagim.loan.service.LoanSimulationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/loan/v1")
@Slf4j
public class LoanApi {

  @Autowired
  private BankAccountsClient client;
  @Autowired
  private LoanSimulationService loanSimulationService;

  @GetMapping(path = "/customer/{customerId}", produces = "application/json")
  @CircuitBreaker(name = "loanBreaker", fallbackMethod = "findCustomerServiceUnReachable")
  public ResponseEntity<?> findCustomerById(@PathVariable String customerId){

    return new ResponseEntity<>(client.getCustomerAccounts(customerId), HttpStatus.OK);
  }

  @GetMapping(path = "/customer", produces = "application/json")
  public ResponseEntity<?> simulateLoanRepaymentPlan(@RequestHeader(required = true) String customerId,
      @RequestHeader(required = true) double loanAmount){

    try {
      return new ResponseEntity<>(loanSimulationService.simulateLoanPaymentPlan(customerId,loanAmount), HttpStatus.OK);
    } catch (BusinessException businessException) {
      return new ResponseEntity<>(businessException.getMessage(), HttpStatus.BAD_REQUEST);
    }

  }

  public ResponseEntity<?> findCustomerServiceUnReachable(Exception exception ){
    log.info("Exception Occurred ..."+exception.getMessage());
    throw new BusinessException("Bank Accounts Service Unreachable !");
//    return new ResponseEntity<>(, HttpStatus.FAILED_DEPENDENCY);
  }

}
