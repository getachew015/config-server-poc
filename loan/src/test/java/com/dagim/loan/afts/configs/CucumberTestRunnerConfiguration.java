package com.dagim.loan.afts.configs;

import com.dagim.loan.LoanApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(
    classes = LoanApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@CucumberContextConfiguration
@ActiveProfiles(profiles = {"test"})
@EmbeddedKafka(
    brokerProperties = {"unclean.leader.election.enabled=true"},
    ports = {9092, 9093, 9094, 9095},
    partitions = 3,
    count = 4)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CucumberTestRunnerConfiguration {}
