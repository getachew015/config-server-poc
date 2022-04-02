package com.customer.accounts.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;
import loan.avro.NewCustomerLoanEvent;

@Service
@Slf4j
public class KafkaAvroMessageConsumer {

    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @KafkaListener(topics = "shine-test-local-avro-topic", id = "", idIsGroup = false, groupId = "shine-local-avro")
    public void listen(NewCustomerLoanEvent message) {
        MessageListenerContainer listenerContainer =
                kafkaListenerEndpointRegistry.getListenerContainer("");
        listenerContainer.stop();
        listenerContainer.start();

        log.info("Received Messasge in group : {}", message);
    }

}
