package com.dagim.loan.afts.configs;

import com.dagim.loan.LoanApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
    ports = {9092},
    partitions = 3,
    count = 1)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
public class CucumberTestRunnerConfiguration {
//    @Bean
//    public MockMvc mockMvc() {
//        return MockMvcBuilders.webAppContextSetup(WebApplicationContextUtils()).build()
//    }
}
