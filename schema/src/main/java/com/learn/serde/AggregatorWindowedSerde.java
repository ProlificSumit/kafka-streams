package com.learn.serde;

import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.kstream.TimeWindowedDeserializer;
import org.apache.kafka.streams.kstream.TimeWindowedSerializer;
import org.apache.kafka.streams.kstream.Windowed;

public class AggregatorWindowedSerde implements Serde<Windowed<String>> {
    public AggregatorWindowedSerde() {
        System.out.println("AggregatorWindowedSerde");
    }

    @Override
    public Serializer<Windowed<String>> serializer() {
        return new TimeWindowedSerializer<>(new StringSerializer());
    }

    @Override
    public Deserializer<Windowed<String>> deserializer() {
        return new TimeWindowedDeserializer<>(new StringDeserializer());
    }
}
