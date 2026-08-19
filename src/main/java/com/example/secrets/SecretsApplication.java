package com.example.secrets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SecretsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecretsApplication.class, args);
    }
}
