package com.learn;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class ProducerApplication {
    public static void main(String[] args) {
        log.info("Starting producer Aggregator");
        SpringApplication.run(ProducerApplication.class, args);
    }
}
