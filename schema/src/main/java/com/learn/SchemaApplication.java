package com.learn;

import com.learn.config.SchemaRegistryConfigContainer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class SchemaApplication {
    public static void main(String[] args) {
        SchemaRegistryConfigContainer.getConfigs("test","test","test");
        SpringApplication.run(SchemaApplication.class, args);
    }
}
