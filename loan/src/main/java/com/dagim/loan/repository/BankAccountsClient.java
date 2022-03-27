package com.dagim.loan.repository;


import com.dagim.loan.configuration.LoanApplicationConfiguration;
import com.dagim.loan.model.BankAccounts;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "bank-accounts", url = "${customer-accounts.api.url}", configuration = LoanApplicationConfiguration.class)
public interface BankAccountsClient {

  @GetMapping(value = "/customer/{customerId}")
  ResponseEntity<List<BankAccounts>> getCustomerAccounts(@PathVariable String customerId);


}
