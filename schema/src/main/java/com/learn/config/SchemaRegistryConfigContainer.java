package com.learn.config;

import com.amazonaws.services.schemaregistry.kafkastreams.AWSKafkaAvroSerDe;
import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
import com.amazonaws.services.schemaregistry.utils.AvroRecordType;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.model.Compatibility;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j2
public class SchemaRegistryConfigContainer {

    private SchemaRegistryConfigContainer() {
    }

    public static Map<String, Object> getConfigs(String schemaOwnerComponent, String environment, String assetId) {
        log.info("SchemaRegistryConfigContainer getConfigs ");
        HashMap<String, Object> config = new HashMap<>();
        config.put(AWSSchemaRegistryConstants.AWS_REGION, Region.US_EAST_1);
        config.put(AWSSchemaRegistryConstants.SCHEMA_AUTO_REGISTRATION_SETTING, "true");
        config.put(AWSSchemaRegistryConstants.SCHEMA_NAME, "a" + assetId + "-entity-usg-" +
                schemaOwnerComponent + "-schema-" + environment.toLowerCase());
        log.info("registry => " + "a" + assetId + "-api-usg-registry-" + environment.toLowerCase());
        config.put(AWSSchemaRegistryConstants.REGISTRY_NAME, "a" + assetId + "-api-usg-registry-"
                + environment.toLowerCase());
        config.put(AWSSchemaRegistryConstants.COMPATIBILITY_SETTING, Compatibility.BACKWARD);
        config.put(AWSSchemaRegistryConstants.DESCRIPTION, schemaOwnerComponent);
        config.put(AWSSchemaRegistryConstants.AVRO_RECORD_TYPE, AvroRecordType.SPECIFIC_RECORD.getName());
        config.put(AWSSchemaRegistryConstants.TAGS, getTags(environment, assetId));
        log.info("Serde is {} ", Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, AWSKafkaAvroSerDe.class.getName());
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "avro-streams");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        config.put("key.deserializer", StringDeserializer.class.getName());
//        config.put("value.deserializer", AWSKafkaAvroSerDe.class.getName());
        return config;
    }

    private static Map<String, String> getTags(String environment, String assetId) {
        log.info("SchemaRegistryConfigContainer getTags");
        Map<String, String> tags = new HashMap<>();
        tags.put("tr:application-asset-insight-id", assetId);
        tags.put("tr:environment-type", environment);
        tags.put("tr:service-name", "a205941-RDP Entity Usage");
        return tags;
    }

    public static Properties getAwsSchemaConfig(String schemaOwnerComponent, String environment, String assetId) {
        Properties config = new Properties();

        config.put(AWSSchemaRegistryConstants.AWS_REGION, Region.US_EAST_1);
        config.put(AWSSchemaRegistryConstants.SCHEMA_AUTO_REGISTRATION_SETTING, "true");
        config.put(AWSSchemaRegistryConstants.REGISTRY_NAME, "a205941-entity-usg-producer-schema-local");
        config.put(AWSSchemaRegistryConstants.SCHEMA_NAME, "a" + assetId + "-entity-usg-" + schemaOwnerComponent + "-schema-" + environment.toLowerCase());
        config.put(AWSSchemaRegistryConstants.COMPATIBILITY_SETTING, Compatibility.BACKWARD);
        config.put(AWSSchemaRegistryConstants.DESCRIPTION, schemaOwnerComponent);
        config.put(AWSSchemaRegistryConstants.AVRO_RECORD_TYPE, AvroRecordType.SPECIFIC_RECORD.getName());
        //config.put(AWSSchemaRegistryConstants.TAGS, getTags(environment, assetId));

//        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, AWSKafkaAvroSerDe.class.getName());
        return config;
    }
}
