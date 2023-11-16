package com.learn.config;

public class EmployeeConsumerSerDe extends CustomAwsKafkaAvroSerDe {
    public EmployeeConsumerSerDe(String schemaName) {
        super("consumer serde");
    }
}
