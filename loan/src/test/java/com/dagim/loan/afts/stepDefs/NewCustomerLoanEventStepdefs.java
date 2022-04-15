package com.dagim.loan.afts.stepDefs;

import com.dagim.loan.kafka.NewCustomerLoanEventPublisher;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import loan.avro.NewCustomerLoanEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class NewCustomerLoanEventStepdefs {

  @Autowired private KafkaTemplate kafkaTemplate;
  @Autowired private NewCustomerLoanEventPublisher newCustomerLoanEventPublisher;

  @Value("${newcustomer.loan-event.topic}")
  private String newCustomerLoanEventTopic;

  private List<NewCustomerLoanEvent> newCustomerLoanEventList;

  @Before
  void setUp() {
    newCustomerLoanEventList = new ArrayList<>();
  }

  @DataTableType
  public NewCustomerLoanEvent newCustomerLoanEvent(Map<String, String> newCustomerLoanEntry) {
    return new NewCustomerLoanEvent(
        newCustomerLoanEntry.get("first_name"),
        newCustomerLoanEntry.get("last_name"),
        Double.parseDouble(newCustomerLoanEntry.get("loan_amount")),
        newCustomerLoanEntry.get("loan_requested_date"));
  }

  @Given("A loan simulation request is submitted by a newUser")
  public void aLoanSimulationRequestIsSubmitted(List<NewCustomerLoanEvent> newCustomerLoan) {
    newCustomerLoanEventList = new ArrayList<>();
    newCustomerLoan.forEach(
        loan -> {
          // can check if customer exists or not in DB and filter the list
          newCustomerLoanEventList.add(loan);
        });
    log.info("AFT capture loan events ... {}", newCustomerLoanEventList.size());
  }

  @When("The requesting customer is a new customer a kafka event will be published")
  public void theRequestingCustomerIsANewCustomerAKafkaEventWillBePublished() {
    newCustomerLoanEventList.forEach(
        loanEvent -> {
          // publish each loan event
          newCustomerLoanEventPublisher.publishLoanEvent(loanEvent);
          // verify published loan event amount
          assertThat(newCustomerLoanEventPublisher.getLoanEvent().getLoanAmount())
              .isEqualTo(loanEvent.getLoanAmount());
        });
    log.info("AFT asserting loan event getting published");
  }

  @Then("The new customer interested in loan event will be processed and saved to data base")
  public void theNewCustomerInterestedInLoanEventWillBeProcessedAndSavedToDataBase() {}
}
