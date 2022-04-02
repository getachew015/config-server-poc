package com.dagim.loan.kafka;

import loan.avro.NewCustomerLoanEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
public class NewCustomerLoanEventPublisher {

  @Value("${newcustomer.loan-event.topic}")
  private String newCustomerLoanEventTopic;

  @Autowired private KafkaTemplate<String, NewCustomerLoanEvent> kafkaTemplate;

  public void publishLoanEvent(NewCustomerLoanEvent newCustomerLoanEvent) {
    ListenableFuture<SendResult<String, NewCustomerLoanEvent>> future =
        kafkaTemplate.send(newCustomerLoanEventTopic, newCustomerLoanEvent);

    future.addCallback(
        new ListenableFutureCallback<>() {

          @Override
          public void onSuccess(SendResult<String, NewCustomerLoanEvent> result) {
            log.info(
                "Sent message=[{}] with offset=[{}]",
                newCustomerLoanEvent,
                result.getRecordMetadata().offset());
          }

          @Override
          public void onFailure(Throwable ex) {
            log.info(
                "Unable to send message=[{}] due to : {}", newCustomerLoanEvent, ex.getMessage());
          }
        });
    //    log.info("New Customer interested in pi4 loan event published {}", newCustomerLoanEvent);
    //    kafkaTemplate.send(newCustomerLoanEventTopic, newCustomerLoanEvent);
  }
}
