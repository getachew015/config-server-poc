package com.dagim.loan.api;

import com.dagim.loan.model.LoanRepaymentPlan;
import com.dagim.loan.model.Loan;
import com.dagim.loan.repository.BankAccountsClient;
import com.dagim.loan.service.LoanSimulationService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class LoanApiTest {

  @Autowired MockMvc mockMvc;
  @MockBean BankAccountsClient client;
  @MockBean private LoanSimulationService simulationService;
  private LoanRepaymentPlan repaymentPlan;
  private Loan loan;
  private Set<LoanRepaymentPlan> repaymentPlanList;

  @Value("${loan.paylater.frequency}")
  private long loanRepaymentFrequency;

  @Value("${loan.paylater.maxAmount}")
  private double maxAllowedLoanAmount;

  @Autowired CircuitBreakerRegistry circuitBreakerRegistry;
  CircuitBreaker loanBreaker;

  @BeforeEach
  public void setUp() {
    loan = new Loan();
    repaymentPlanList = new HashSet<>();
    loanBreaker =
        circuitBreakerRegistry
            .getAllCircuitBreakers()
            .filter(circuitBreaker -> circuitBreaker.getName().equalsIgnoreCase("loanBreaker"))
            .get();
  }

  @AfterEach
  public void tearDown() {}

  @Test
  @DisplayName("Test Should pass when a right customerId is passed as input")
  public void findCustomerByIdTest() {
    log.info("BUFFERED CALLS .... {}", loanBreaker.getMetrics().getFailureRate());
    assertThat(loanBreaker.getName()).isEqualTo("loanBreaker");
    assertThat(loanBreaker).isNotNull();
  }

  @RepeatedTest(10)
  @DisplayName("Test Should pass for all cases of circuit breaker CLOSED OPEN HALF_OPEN")
  public void findCustomerByIdTest(RepetitionInfo info) throws Exception {
    String customerId = "cust1001";
    if (info.getCurrentRepetition() < 3) {
      when(client.getCustomerAccounts(customerId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
      mockMvc
          .perform(
              MockMvcRequestBuilders.get("/loan/v1/customer/" + customerId)
                  .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
    if (info.getCurrentRepetition() < 5)
      assertThat(loanBreaker.getState())
          .isEqualTo(
              CircuitBreaker.State
                  .CLOSED); // checked circuit stays closed till failure threshold exceeds
    if (info.getCurrentRepetition() > 2 && info.getCurrentRepetition() < 6) {
      when(client.getCustomerAccounts(customerId)).thenThrow(feign.RetryableException.class);
      mockMvc
          .perform(
              MockMvcRequestBuilders.get("/loan/v1/customer/" + customerId)
                  .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().is4xxClientError());
      if (info.getCurrentRepetition() == 5) {
        assertThat(loanBreaker.getMetrics().getNumberOfFailedCalls())
            .isEqualTo(3); // checked number of failures are counted as registered
        assertThat(loanBreaker.getMetrics().getFailureRate())
            .isEqualTo(60.0f); // checked failure rate is calculated and will cause circuit to open
        assertThat(loanBreaker.getState())
            .isEqualTo(CircuitBreaker.State.OPEN); // checked circuit breaker tripped and opened
      }
    }
    if (info.getCurrentRepetition() == 6) {
      Thread.sleep(5100);
      assertThat(loanBreaker.getState())
          .isEqualTo(
              CircuitBreaker.State
                  .HALF_OPEN); // checked circuit breaker changed to HALF_OPEN state after 5s
    }
    if (info.getCurrentRepetition() > 5) {
      when(client.getCustomerAccounts(customerId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
      mockMvc
          .perform(
              MockMvcRequestBuilders.get("/loan/v1/customer/" + customerId)
                  .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
      if (info.getCurrentRepetition() >= 8)
        assertThat(loanBreaker.getState())
            .isEqualTo(
                CircuitBreaker.State
                    .CLOSED); // checked circuit breaker went back to closed after 2 successful
      // responses
    }
    log.info(
        "CIRCUIT BREAKER STATE IS {} AT REP {} .... ",
        loanBreaker.getState().name(),
        info.getCurrentRepetition());
    log.info(
        "BUFFERED CALLS {} FAILURE RATE {} .... ",
        loanBreaker.getMetrics().getNumberOfBufferedCalls(),
        loanBreaker.getMetrics().getFailureRate());
  }

  @Test
  @DisplayName("Test Should pass when a right loan amount is passed")
  public void simulateLoanRepaymentPlanTest() {}
}
