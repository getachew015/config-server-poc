package com.customer.accounts.service;

import com.customer.accounts.exception.BusinessException;
import com.customer.accounts.model.BankAccounts;
import com.customer.accounts.model.Customer;
import com.customer.accounts.repository.BankAccountsRepository;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.customer.accounts.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankAccountsService {

  @Value("${accounts.loan.maxAmount}")
  private double loanAccountDefault;
  @Value("${accounts.checkings.minAmount}")
  private double checkingsAccountDefault;
  @Value("${accounts.savings.minAmount}")
  private double savingsAccountDefault;

  private enum ACCOUNTTYPE {CHECKING,SAVING,LOAN};

  @Autowired
  private BankAccountsRepository bankAccountsRepository;
  @Autowired
  private CustomerRepository customerRepository;


  public BankAccountsService(){
  }

  public boolean isNewCustomer(String customerId){
    return customerRepository.customerExistsByCustomerId(customerId);
  }

  public List<BankAccounts> getCustomerAccounts(String customerId){
    return bankAccountsRepository.findAccountsByCustomerId(customerId);
  }

  public BankAccounts customerAccountByType(String customerId, String accountType){

    return bankAccountsRepository.findAccountsByCustomerId(customerId).stream()
            .filter(acct -> acct.getAccountType().equals(accountType)).findFirst().get();
  }

  public Customer createCustomerAccount(Customer customer, String accountType){

    Set<BankAccounts> accountsSet = new HashSet<>();
    BankAccounts account = new BankAccounts();

    if(customerRepository.customerExistsByCustomerId(customer.getCustomerId())){
      if(bankAccountsRepository.accountExistsByCustomerId(customer.getCustomerId(), accountType))
        throw new BusinessException(HttpStatus.BAD_REQUEST,"Duplicate Account Opening Requested!" );
      else{
        account.setCustomerId(customer.getCustomerId());
        account.setAccountOpenDate(OffsetDateTime.now());
        account.setAccountType(accountType);
        if(accountType.equals(ACCOUNTTYPE.CHECKING.toString()))
          account.setAccountBalance(checkingsAccountDefault);
        else if(accountType.equals(ACCOUNTTYPE.SAVING.toString()))
          account.setAccountBalance(savingsAccountDefault);
        else if(accountType.equals(ACCOUNTTYPE.LOAN.toString()))
          account.setAccountBalance(loanAccountDefault);
        bankAccountsRepository.saveAndFlush(account);
        return customerRepository.findCustomerByCustomerId(customer.getCustomerId());
      }
    }else{
      account.setAccountType(accountType);
      account.setAccountOpenDate(OffsetDateTime.now());
      account.setCustomerId(customer.getCustomerId());
      if(accountType.equals(ACCOUNTTYPE.CHECKING.toString()))
        account.setAccountBalance(checkingsAccountDefault);
      else if(accountType.equals(ACCOUNTTYPE.SAVING.toString()))
        account.setAccountBalance(savingsAccountDefault);
      else if(accountType.equals(ACCOUNTTYPE.LOAN.toString()))
        account.setAccountBalance(loanAccountDefault);

      accountsSet.add(account);
      customer.setBankAccounts(accountsSet);
      return customerRepository.saveAndFlush(customer);
    }

  }

}
