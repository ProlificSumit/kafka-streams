package com.learn.config;

import com.amazonaws.services.schemaregistry.kafkastreams.AWSKafkaAvroSerDe;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class CustomAwsKafkaAvroSerDe extends AWSKafkaAvroSerDe {
    public static final String ENVIRONMENT_CONFIG_NAME = "environment";
    public static final String ASSET_ID_CONFIG_NAME = "assetId";

    private final String schemaName;

    public CustomAwsKafkaAvroSerDe(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public void configure(Map<String, ?> serdeConfig, boolean isSerdeForRecordKeys) {
        Map<String, Object> configs = amendConfiguration(new HashMap<>(serdeConfig));
        if (log.isDebugEnabled()) {
            log.debug("SerDe configuration isSerdeForRecordKeys : {} ", isSerdeForRecordKeys);
        }
        super.configure(configs, isSerdeForRecordKeys);
    }

    private Map<String, Object> amendConfiguration(HashMap<String, ?> serdeConfig) {
        Map<String, Object> configs = new HashMap<>(serdeConfig);
        if (!configs.containsKey(ENVIRONMENT_CONFIG_NAME)
                || !configs.containsKey(ASSET_ID_CONFIG_NAME)) {
            throw new IllegalArgumentException("Missing required 'environment' and 'assetId' parameters");
        }
        String env = (String) configs.get(ENVIRONMENT_CONFIG_NAME);
        String assetId = (String) configs.get(ASSET_ID_CONFIG_NAME);
        configs.putAll(SchemaRegistryConfigContainer.getConfigs(schemaName, env, assetId));
        return configs;
    }
}
