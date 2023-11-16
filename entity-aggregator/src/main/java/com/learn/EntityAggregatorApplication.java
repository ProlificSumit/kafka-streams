package com.learn;

import com.learn.schema.record.EmployeeAggregatedRecord;
import com.learn.schema.record.EmployeeExpenseDetails;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.function.Function;

@Log4j2
@SpringBootApplication
public class EntityAggregatorApplication {

    public static void main(String[] args) {
        log.info("Starting Entity Aggregator");
        SpringApplication.run(EntityAggregatorApplication.class, args);
    }

    @Bean
    public Function<KStream<String, EmployeeExpenseDetails>, KStream<Windowed<String>, EmployeeAggregatedRecord>> process() {
        log.info("received stream ");
        return stream -> stream.groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(5)))
                .aggregate(EmployeeAggregatedRecord::new, (key, value, aggregate) -> {
                    log.info("key {}, value {}, aggregate {}", key, value, aggregate);
                    return aggregate;
                })
                .toStream()
                .peek((key, value) -> log.info("key {}, value {} ", key, value));
    }

}
