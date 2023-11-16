package com.learn.serde;

import com.learn.config.CustomAwsKafkaAvroSerDe;

public class AggregatorAvroSerde extends CustomAwsKafkaAvroSerDe {
    public AggregatorAvroSerde(String schemaName) {
        super("aggregator");
    }
}
