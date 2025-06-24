package com.skysoft.app.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
  private final Jwt jwt = new Jwt();

  @Getter
  @Setter
  public static class Jwt {
    private String urlRoot;
    private String urlAuth;
    private String username;
    private String password;
  }
}
