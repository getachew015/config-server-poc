package com.dagim.loan.afts.stepDefs;

import io.cucumber.java.BeforeAll;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import loan.avro.NewCustomerLoanEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.Map;

@Slf4j
public class NewCustomerLoanEventStepdefs {

  @Autowired private KafkaTemplate kafkaTemplate;

  @Value("${newcustomer.loan-event.topic}")
  private String newCustomerLoanEventTopic;

  @BeforeAll
  void setUp() {}

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
    newCustomerLoan.forEach(
        loan -> {
          sendKafkaMessage(loan);
        });
  }

  @When("The requesting customer is a new customer a kafka event will be published")
  public void theRequestingCustomerIsANewCustomerAKafkaEventWillBePublished() {}

  @Then("The new customer interested in loan event will be processed and saved to data base")
  public void theNewCustomerInterestedInLoanEventWillBeProcessedAndSavedToDataBase() {}

  public void sendKafkaMessage(NewCustomerLoanEvent newCustomerLoanEvent) {
    ListenableFuture<SendResult<String, NewCustomerLoanEvent>> future =
        kafkaTemplate.send(newCustomerLoanEventTopic, newCustomerLoanEvent);

    future.addCallback(
        new ListenableFutureCallback<>() {

          @Override
          public void onSuccess(SendResult<String, NewCustomerLoanEvent> result) {
            log.info(
                "AFT Sent message=[{}] with offset=[{}]",
                newCustomerLoanEvent,
                result.getRecordMetadata().offset());
          }

          @Override
          public void onFailure(Throwable ex) {
            log.info(
                "AFT Unable to send message=[{}] due to : {}", newCustomerLoanEvent, ex.getMessage());
          }
        });
  }
}
