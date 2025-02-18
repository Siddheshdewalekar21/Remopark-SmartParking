package com.remopark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class RemoparkApplication {
    public static void main(String[] args) {
        SpringApplication.run(RemoparkApplication.class, args);
    }
} 