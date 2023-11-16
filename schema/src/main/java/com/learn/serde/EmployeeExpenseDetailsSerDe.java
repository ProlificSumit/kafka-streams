package com.learn.serde;

import com.learn.config.CustomAwsKafkaAvroSerDe;

public class EmployeeExpenseDetailsSerDe extends CustomAwsKafkaAvroSerDe {
    public EmployeeExpenseDetailsSerDe(String schemaName) {
        super("producerSerde called");
    }
}
