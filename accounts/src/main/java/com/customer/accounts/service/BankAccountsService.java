package com.customer.accounts.service;

import com.customer.accounts.model.BankAccounts;
import com.customer.accounts.repository.BankAccountsRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConfigurationProperties
public class BankAccountsService {

  @Value("${accounts.loan.maxAmount}")
  private double loanAccountDefault;
  @Value("${accounts.loan.minAmount}")
  private double checkingsAccountDefault;
  @Value("${accounts.loan.minAmount}")
  private double savingsAccountDefault;
  private enum ACCOUNTTYPE {CHECKING,SAVING,LOAN};

  @Autowired
  private BankAccountsRepository bankAccountsRepository;

  public BankAccountsService(){
    log.info("Default Loan ... "+loanAccountDefault);
    log.info("Default Checking ... "+checkingsAccountDefault);
    log.info("Default Savings ... "+savingsAccountDefault);
  }

  public boolean isNewCustomer(String customerId){
    return bankAccountsRepository.customerExistsByCustomerId(customerId);
  }

  public List<BankAccounts> getCustomerAccounts(String customerId){
    return bankAccountsRepository.findAccountsByCustomerId(customerId);
  }

  public BankAccounts createAccounts(String customerId, String accountType){

    BankAccounts accounts = new BankAccounts();
    accounts.setCustomerId(customerId);
    accounts.setAccountType(accountType);
    accounts.setAccountOpenDate(OffsetDateTime.now());

    if(accountType.equals(ACCOUNTTYPE.CHECKING))
      accounts.setAccountBalance(checkingsAccountDefault);
    else if(accountType.equals(ACCOUNTTYPE.SAVING))
      accounts.setAccountBalance(savingsAccountDefault);
    else if(accountType.equals(ACCOUNTTYPE.LOAN))
      accounts.setAccountBalance(loanAccountDefault);

    return bankAccountsRepository.saveAndFlush(accounts);
  }


}
