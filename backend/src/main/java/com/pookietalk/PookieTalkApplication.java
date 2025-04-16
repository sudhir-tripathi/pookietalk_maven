package com.pookietalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class PookieTalkApplication {

    private static final Logger logger = LoggerFactory.getLogger(PookieTalkApplication.class);

    public static void main(String[] args) {
        logger.info("Starting PookieTalk Application...");

        SpringApplication app = new SpringApplication(PookieTalkApplication.class);

        // Optional: Add shutdown hook or custom initializers here
        Runtime.getRuntime().addShutdownHook(new Thread(() -> 
            logger.info("PookieTalk Application is shutting down gracefully.")
        ));

        app.run(args);

        logger.info("PookieTalk Application started successfully.");
    }
}
