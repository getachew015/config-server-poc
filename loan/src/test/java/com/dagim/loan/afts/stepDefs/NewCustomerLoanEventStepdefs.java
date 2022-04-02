package com.dagim.loan.afts.stepDefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewCustomerLoanEventStepdefs {

  @Given("A loan simulation request is submitted")
  public void aLoanSimulationRequestIsSubmitted() {}

  @When("The requesting customer is a new customer a kafka event will be published")
  public void theRequestingCustomerIsANewCustomerAKafkaEventWillBePublished() {}

  @Then("The new customer interested in loan event will be processed and saved to data base")
  public void theNewCustomerInterestedInLoanEventWillBeProcessedAndSavedToDataBase() {}
}
