package com.customer.accounts.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import loan.avro.NewCustomerLoanEvent;

@Service
@Slf4j
public class KafkaAvroMessageConsumer {

    @KafkaListener(topics = "shine-test-local-avro-topic", groupId = "shine-local-avro")
    public void listen(NewCustomerLoanEvent message) {
        log.info("Received Messasge in group : {}", message);
    }

}
