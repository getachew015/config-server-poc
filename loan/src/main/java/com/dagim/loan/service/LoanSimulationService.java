package com.dagim.loan.service;

import com.dagim.loan.configuration.LoanConfigDetail;
import com.dagim.loan.exception.BusinessException;
import com.dagim.loan.kafka.NewCustomerLoanEventPublisher;
import com.dagim.loan.model.BankAccounts;
import com.dagim.loan.model.Loan;
import com.dagim.loan.model.LoanRepaymentPlan;
import com.dagim.loan.repository.BankAccountsClient;
import com.dagim.loan.repository.LoanSimulationRepository;
import loan.avro.NewCustomerLoanEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoanSimulationService {

  @Autowired private BankAccountsClient bankAccountsClient;
  @Autowired private LoanSimulationRepository loanSimulationRepository;
  @Autowired private LoanConfigDetail loanConfigDetail;
  @Autowired private NewCustomerLoanEventPublisher newCustomerLoanEventPublisher;

  private enum ACCOUNTTYPE {
    CHECKING,
    SAVING,
    LOAN
  };

  private enum INSTALLMENTTATE {
    ACTIVE,
    SIMULATION,
    PAIDOFF
  };

  public LoanSimulationService() {}

  public Loan simulateLoanPaymentPlan(String customerId, double loanAmount) {
    return loanSimulationRepository.saveAndFlush(
        getLoanPaymentPlan(customerId, loanAmount, INSTALLMENTTATE.SIMULATION.toString()));
  }

  public Loan requestPayInFourLoan(
      String customerId, String firstName, String lastName, double loanAmount) {
    List<BankAccounts> accounts =
        getCustomerAccounts(customerId).stream()
            .filter(
                acct ->
                    acct.getAccountType().equals(ACCOUNTTYPE.CHECKING.toString())
                        || acct.getAccountType().equals(ACCOUNTTYPE.SAVING.toString()))
            .collect(Collectors.toList());
    log.info("accounts list {}", accounts.toString());
    if (!accounts.isEmpty())
      return loanSimulationRepository.saveAndFlush(
          getLoanPaymentPlan(customerId, loanAmount, INSTALLMENTTATE.ACTIVE.toString()));
    else {
      // Publish New Customer Interested in Loan EVENT
      publishNewCustomerLoanEvent(
          new NewCustomerLoanEvent(firstName, lastName, loanAmount, LocalDate.now().toString()));
      throw new BusinessException(
          HttpStatus.BAD_REQUEST, "Customer Not Qualified For Pay In Four Loan!");
    }
  }

  private List<BankAccounts> getCustomerAccounts(String customerId) {
    if (bankAccountsClient.getCustomerAccounts(customerId).getStatusCode().is2xxSuccessful()) {
      List<BankAccounts> accts = bankAccountsClient.getCustomerAccounts(customerId).getBody();
      log.info("response body ... {}", accts.toString());
      return accts;
    } else
      throw new BusinessException(
          bankAccountsClient.getCustomerAccounts(customerId).getStatusCode(),
          "No Accounts Found For Customer!");
  }

  private Loan getLoanPaymentPlan(String customerId, double loanAmount, String installmentState) {
    Loan loan = new Loan();
    LoanRepaymentPlan repaymentPlan;
    Set<LoanRepaymentPlan> repaymentPlanList = new HashSet<>();
    OffsetDateTime lastPaymentScheduledDay = OffsetDateTime.now();
    for (int i = 0; i < loanConfigDetail.getFrequency(); i++) {
      lastPaymentScheduledDay = lastPaymentScheduledDay.plusDays(14);
      repaymentPlan = new LoanRepaymentPlan();
      repaymentPlan.setLoanAmount(loanAmount / loanConfigDetail.getFrequency());
      repaymentPlan.setLoanDueDate(lastPaymentScheduledDay);
      repaymentPlanList.add(repaymentPlan);
    }
    loan.setLoanRequestDate(OffsetDateTime.now());
    loan.setCustomerId(customerId);
    loan.setLoanAmount(loanAmount);
    loan.setInstallmentState(installmentState);
    loan.setRepaymentPlan(repaymentPlanList);
    return loanSimulationRepository.saveAndFlush(loan);
  }

  private void publishNewCustomerLoanEvent(NewCustomerLoanEvent newCustomerLoanEvent) {
    newCustomerLoanEventPublisher.publishLoanEvent(newCustomerLoanEvent);
  }
}
