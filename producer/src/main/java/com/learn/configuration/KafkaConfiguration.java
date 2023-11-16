package com.learn.configuration;

import com.amazonaws.services.schemaregistry.serializers.avro.AWSKafkaAvroSerializer;
import com.learn.config.SchemaRegistryConfigContainer;
import com.learn.schema.record.EmployeeExpenseDetails;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Log4j2
@Configuration
@EnableConfigurationProperties({KafkaProperties.class})
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;

    @Value("${usage.topic.name}")
    private String topicName;

    @Value("${usage.topic.groupId}")
    private String groupId;

    @Value("${usage.topic.partition-num}")
    private Integer partition;

    @Value("${usage.topic.replication-factor}")
    private Short replicationFactor;

    @Value("${environment}")
    private String environment;

    @Value("${assetId}")
    private String assetId;

    @Value("${usage.topic.unclean-leader-election-enabled}")
    private String leaderElectionEnabled;

    @Value("${usage.topic.retention-ms}")
    private String retentionMs;

    @Value("${usage.topic.compressionType}")
    private String compressionType;

    @Value("${usage.topic.ack}")
    private String ack;

    @Value("${usage.topic.retries}")
    private String retries;

    @Value("${usage.topic.retry.backoff-ms}")
    private String retryBackoffMs;

    @Value("${usage.topic.max.block-ms}")
    private String maxBlockMs;

    @Value("${usage.topic.metadata.max.age-ms}")
    private String metadataMaxAgeMs;

    @Value("${usage.topic.buffer.memory}")
    private String bufferMemory;

    @Value("${usage.topic.delivery.timeout-ms}")
    private String deliveryTimeoutMs;

    @Value("${usage.topic.request.timeout-ms}")
    private String requestTimeoutMs;

    public KafkaConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public NewTopic entityUsageTopic() {
        return new NewTopic(topicName, partition, replicationFactor);
    }

    @Bean
    ProducerFactory<String, EmployeeExpenseDetails> producerFactory() {
        Map<String, Object> props = kafkaProperties.buildAdminProperties();
        props.put("groupId", groupId);
        props.putAll(SchemaRegistryConfigContainer.getConfigs("producer", environment, assetId));
        setCompression(props);
        setAcknowledgements(props);
        setRetryConfig(props);
        log.info("Creating kafka producer factory with properties : {} ", props);
        return new DefaultKafkaProducerFactory<>(props);
    }

    private void setRetryConfig(Map<String, Object> properties) {
        properties.put(ProducerConfig.RETRIES_CONFIG, retries);
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackoffMs);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, maxBlockMs);
        properties.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, metadataMaxAgeMs);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AWSKafkaAvroSerializer.class.getName());
    }

    private void setAcknowledgements(Map<String, Object> properties) {
        // Set number of acknowledgements - asks - default is all
        properties.put(ProducerConfig.ACKS_CONFIG, ack);
    }

    private void setCompression(Map<String, Object> properties) {
        // Use Snappy compression for batch compression
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
    }

    @Bean
    public KafkaTemplate<String, EmployeeExpenseDetails> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
