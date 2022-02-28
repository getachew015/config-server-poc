package com.dagim.loan.service;

import com.dagim.loan.exception.BusinessException;
import com.dagim.loan.model.LoanRepaymentPlan;
import com.dagim.loan.model.SimulatedLoan;
import com.dagim.loan.repository.BankAccountsClient;
import com.dagim.loan.repository.LoanSimulationRepository;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class LoanSimulationService {

  @Autowired
  private BankAccountsClient bankAccountsClient;
  @Autowired
  private LoanSimulationRepository loanSimulationRepository;
  @Value("${loan.paylater.frequency}")
  private long loanRepaymentFrequency;
  @Value("${loan.paylater.maxAmount}")
  private double maxAllowedLoanAmount;

  public LoanSimulationService(){

  }
  public SimulatedLoan simulateLoanPaymentPlan(String customerId, double loanAmount) {
    SimulatedLoan simulatedLoan = new SimulatedLoan();
    LoanRepaymentPlan repaymentPlan;
    Set<LoanRepaymentPlan> repaymentPlanList = new HashSet<>();
    OffsetDateTime lastPaymentScheduledDay = OffsetDateTime.now();
    if(loanAmount < maxAllowedLoanAmount){
        for (int i=0; i < loanRepaymentFrequency; i++){
          lastPaymentScheduledDay = lastPaymentScheduledDay.plusDays(14);
          repaymentPlan = new LoanRepaymentPlan();
          repaymentPlan.setLoanAmount(loanAmount/loanRepaymentFrequency);
          repaymentPlan.setLoanDueDate(lastPaymentScheduledDay);
          repaymentPlanList.add(repaymentPlan);
        }
        simulatedLoan.setLoanSimulationDate(OffsetDateTime.now());
        simulatedLoan.setCustomerId(customerId);
        simulatedLoan.setLoanAmount(loanAmount);
        simulatedLoan.setRepaymentPlan(repaymentPlanList);
        return loanSimulationRepository.saveAndFlush(simulatedLoan);
    }else
      throw new BusinessException(HttpStatus.BAD_REQUEST, "Loan Amount Exceeded Max Threshold!");
  }

  public ResponseEntity<?> getCustomerAccounts(String customerId){
    return bankAccountsClient.getCustomerAccounts(customerId);
  }
}
