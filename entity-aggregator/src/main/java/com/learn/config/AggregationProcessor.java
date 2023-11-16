package com.learn.config;

import com.amazonaws.services.schemaregistry.kafkastreams.AWSKafkaAvroSerDe;
import com.learn.schema.record.EmployeeAggregatedRecord;
import com.learn.serde.AggregatorAvroSerde;
import com.learn.serde.AggregatorWindowedSerde;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;

@Log4j2
public class AggregationProcessor {
    public static void main(String[] args) {
        KafkaProperties kafkaProperties = new KafkaProperties();
        Map<String, Object> config = kafkaProperties.buildAdminProperties();
        config.putAll(SchemaRegistryConfigContainer.getConfigs("aggregator", "local", "205941"));

        Properties streamConfigs = getConfigs();
        streamConfigs.putAll(config);
        StreamsBuilder builder = new StreamsBuilder();
        KStream<Object, Object> kStream = builder.stream("entity-usage-topic-5");
        log.info("check {} ", TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1)));

        kStream.foreach((key, value) -> log.info("key : {} , value : {}", key, value));
        kStream.groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1)))
                .aggregate(() -> {
                            EmployeeAggregatedRecord aggregatedRecord = new EmployeeAggregatedRecord();
                            aggregatedRecord.setEmployeeAggregatedId("test");
                            aggregatedRecord.setDesignation("test");
                            aggregatedRecord.setEmployeeName("test");
                            aggregatedRecord.setJoiningDate("test");
                            return aggregatedRecord;
                        },
                        (key, value, aggregate) -> {
                            log.info("aggregate key {} , value {} , aggregate {}", key, value, aggregate);
                            return aggregate;
                        })
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .peek((key, value) -> log.info("peek key {} , value {}", key, value))
                .to("entity-out-topic-3");

        KafkaStreams streams = new KafkaStreams(builder.build(), streamConfigs);
        streams.start();

    }

    @NotNull
    private static Properties getConfigs() {
        Properties streamConfigs = new Properties();
        streamConfigs.put(StreamsConfig.APPLICATION_ID_CONFIG, "aggr-sample");
        streamConfigs.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

//        streamConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serdes.StringSerde.class.getName());
//        streamConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AWSKafkaAvroSerDe.class.getName());
//
//        streamConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, AggregatorWindowedSerde.class.getName());
//        streamConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EmployeeAggregatedRecord.class.getName());

        streamConfigs.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, AggregatorWindowedSerde.class.getName());
        streamConfigs.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, AggregatorAvroSerde.class.getName());
        return streamConfigs;
    }
}
