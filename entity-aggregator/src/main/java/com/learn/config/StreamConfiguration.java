package com.learn.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Log4j2
//@Configuration
public class StreamConfiguration {
    @Value("${env}")
    private String env;
    @Value("${assetId}")
    private String assetId;

    //@Bean(name = "aggrApp")
    public StreamsBuilderFactoryBean kafkaStreamsConfiguration() {
        log.info("kafkaStreamsConfiguration start");
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(getProps()));
    }

    public Map<String, Object> getProps() {
        Map<String, Object> configs = new HashMap<>();
        configs.putAll(SchemaRegistryConfigContainer.getConfigs("aggr", "dev", "2056"));
        return configs;
    }
}
