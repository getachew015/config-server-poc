package com.dagim.loan;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    glue = {"com.dagim.loan.afts.stepDefs"},
    features = {"src/test/resources/features"},
    plugin = {"pretty", "html:build/reports/tests/cucumber/cucumber-report.html"})
public class LoanApplicationFunctionalTests {}
