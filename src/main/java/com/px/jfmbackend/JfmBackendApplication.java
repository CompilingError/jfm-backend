/* (C)2026 */
package com.px.jfmbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
public class JfmBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JfmBackendApplication.class, args);
    }
}
