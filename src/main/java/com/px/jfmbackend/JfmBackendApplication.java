/* (C)2026 */
package com.px.jfmbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JfmBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(JfmBackendApplication.class, args);
  }
}
