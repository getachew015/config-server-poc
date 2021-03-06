package com.dagim.loan.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Slf4j
public class AvroSerializer<T extends SpecificRecordBase> implements Serializer<T> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    Serializer.super.configure(configs, isKey);
  }

  @Override
  public byte[] serialize(String s, T payload) {
    byte[] bytes = null;
    try {
      if (payload != null) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryEncoder binaryEncoder =
            EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(payload.getSchema());
        datumWriter.write(payload, binaryEncoder);
        binaryEncoder.flush();
        byteArrayOutputStream.close();
        bytes = byteArrayOutputStream.toByteArray();
        log.info("serialized payload='{}'", DatatypeConverter.printHexBinary(bytes));
      }
    } catch (Exception e) {
      log.error("Unable to serialize payload ", e);
    }
    return bytes;
  }

  @Override
  public byte[] serialize(String topic, Headers headers, T data) {
    return Serializer.super.serialize(topic, headers, data);
  }

  @Override
  public void close() {
    Serializer.super.close();
  }
}
