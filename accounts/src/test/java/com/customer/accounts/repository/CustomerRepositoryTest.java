package com.customer.accounts.repository;

import com.customer.accounts.model.BankAccounts;
import com.customer.accounts.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(profiles = "test")
class CustomerRepositoryTest {

  @Autowired private CustomerRepository customerRepository;

  private Customer customer;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void customerExistsByCustomerId() {
    createDbRecords();
    customerRepository.saveAndFlush(customer);
    assertThat(customerRepository.customerExistsByCustomerId("cust2001")).isTrue();
  }

  @Test
  void findCustomerByCustomerId() {}

  private void createDbRecords() {

    customer =
        Customer.builder()
            .customerRefNumber("0987hrt001")
            .customerId("cust2001")
            .custFirstName("Jhon")
            .custLastName("Doe")
            .bankAccounts(
                Set.of(
                    BankAccounts.builder()
                        .customerId("cust2001")
                        .accountNumber("act1001")
                        .accountType("SAVING")
                        .accountBalance(1500.12)
                        .accountOpenDate(OffsetDateTime.now().minusMonths(4))
                        .build(),
                    BankAccounts.builder()
                        .customerId("cust2001")
                        .accountNumber("act1002")
                        .accountType("CHECKING")
                        .accountBalance(567.78)
                        .accountOpenDate(OffsetDateTime.now().minusMonths(2))
                        .build()))
            .build();
  }
}
