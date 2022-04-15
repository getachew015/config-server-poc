package com.dagim.loan.kafka;

import loan.avro.NewCustomerLoanEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

@Slf4j
@Getter
@Setter
@Service
public class NewCustomerLoanEventPublisher {

  @Value("${newcustomer.loan-event.topic}")
  private String newCustomerLoanEventTopic;

  private NewCustomerLoanEvent loanEvent;

  @Autowired private KafkaTemplate<String, NewCustomerLoanEvent> kafkaTemplate;

  public void publishLoanEvent(NewCustomerLoanEvent newCustomerLoanEvent) {
    try {
      sendMessage(newCustomerLoanEvent);
    } catch (Exception exception) {
        log.info("Unable to send message due to : {}", exception.getMessage());
    }
  }

  private void sendMessage(NewCustomerLoanEvent newCustomerLoanEvent)
      throws ExecutionException, InterruptedException {
    ListenableFuture<SendResult<String, NewCustomerLoanEvent>> listenableFuture =
        kafkaTemplate.send(newCustomerLoanEventTopic, newCustomerLoanEvent);
    loanEvent = listenableFuture.get().getProducerRecord().value();
    listenableFuture.addCallback(
        new ListenableFutureCallback<>() {

          @Override
          public void onSuccess(SendResult<String, NewCustomerLoanEvent> result) {
            log.info(
                "Sent message=[{}] with offset=[{}]",
                result.getProducerRecord().value(),
                result.getRecordMetadata().offset());
          }

          @Override
          public void onFailure(Throwable ex) {
            log.info(
                "Unable to send message=[{}] due to : {}", newCustomerLoanEvent, ex.getMessage());
          }
        });
  }
}
