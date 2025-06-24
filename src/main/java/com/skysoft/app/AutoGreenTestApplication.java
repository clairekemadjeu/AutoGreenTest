package com.skysoft.app;

import com.skysoft.app.web.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class AutoGreenTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(AutoGreenTestApplication.class, args);
  }
}
