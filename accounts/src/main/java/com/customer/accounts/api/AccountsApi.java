package com.customer.accounts.api;


import com.customer.accounts.service.BankAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/accounts/v1")
public class AccountsApi {

  @Autowired
  private BankAccountsService accountsService;

  @GetMapping(path = "/customer/{customerId}")
  public ResponseEntity<?> getCustomerAccounts(@PathVariable String customerId){

    return new ResponseEntity<>(accountsService.getCustomerAccounts(customerId), HttpStatus.OK);
  }

  @GetMapping(path = "/customer", produces = "application/json")
  public ResponseEntity<?> createCustomerAccount(@RequestHeader(required = true) String customerId,
      @RequestHeader(required = true) String accountType){

    return new ResponseEntity<>(accountsService.createAccounts(customerId, accountType), HttpStatus.OK);
  }


}
