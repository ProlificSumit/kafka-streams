package com.learn.serde;


import com.learn.schema.record.EmployeeExpenseDetails;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CustomEmployeeDeserializer implements Deserializer {
    @Override
    public EmployeeExpenseDetails deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }

            System.out.println("data length: " + data.length);
            System.out.println("data content ");

            for (byte b : data) {
                System.out.print(String.format("%02X ", b));
            }
            System.out.println();  // New line for better readability

            if (data.length <= 4) {
                System.out.println("Not enough data for deserialization");
                return null;
            }


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            BinaryDecoder binaryDecoder = DecoderFactory.get().directBinaryDecoder(byteArrayInputStream, null);

            // Skip the schema ID (4 bytes) used by AWS Glue Schema Registry
            binaryDecoder.skipFixed(38);

            SpecificDatumReader<EmployeeExpenseDetails> reader = new SpecificDatumReader<>(EmployeeExpenseDetails.getClassSchema());

            return reader.read(null, binaryDecoder);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing Avro message", e);
        }
    }
}
