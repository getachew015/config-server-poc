package com.dagim.loan.configuration;

import com.dagim.loan.kafka.AvroSerializer;
import com.dagim.loan.model.BankAccounts;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import loan.avro.NewCustomerLoanEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class LoanApplicationConfiguration {

  @Value("${newcustomer.loan-event.topic}")
  private String newCustomerLoanEventTopic;

  @Autowired private KafkaConfigProperties kafkaConfigProperties;

  @Bean
  public ProducerFactory<String, NewCustomerLoanEvent> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092, localhost:9093, localhost:9094");
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, NewCustomerLoanEvent> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public NewTopic createNewCustomerLoanTopic() {
    return TopicBuilder.name(newCustomerLoanEventTopic).partitions(10).replicas(3).build();
  }

  @Bean
  public Decoder feignDecoder() {
    return new JacksonDecoder();
  }
  @Bean
  public ObjectMapper objectMapper(){
    return new ObjectMapper().findAndRegisterModules();
  }

}
