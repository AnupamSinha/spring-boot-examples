package com.anupam.streams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaStreamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamsApplication.class, args);
    }
}
