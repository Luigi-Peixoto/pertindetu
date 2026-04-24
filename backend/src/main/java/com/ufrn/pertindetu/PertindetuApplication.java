package com.ufrn.pertindetu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Pertindetu application.
 * <p>
 * Enables async processing, scheduling, caching, and Feign clients.
 * Excludes default Spring Security user details autoconfiguration.
 * </p>
 */

@SpringBootApplication
public class PertindetuApplication {

    public static void main(String[] args) {
        SpringApplication.run(PertindetuApplication.class, args);
    }

}
