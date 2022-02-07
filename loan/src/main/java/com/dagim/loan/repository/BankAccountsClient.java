package com.dagim.loan.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "bank-accounts", url = "${customer-accounts.api.url}")
public interface BankAccountsClient {

  @GetMapping(value = "/customer/{customerId}")
  ResponseEntity<?> getCustomerAccounts(@PathVariable String customerId);


}
