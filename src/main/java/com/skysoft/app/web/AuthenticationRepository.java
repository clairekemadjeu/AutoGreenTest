package com.skysoft.app.web;

import com.skysoft.app.exception.TechnicalErrorException;
import com.skysoft.app.web.config.ApplicationProperties;
import com.skysoft.app.web.model.AuthenticationRequest;
import com.skysoft.app.web.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationRepository {
  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;

  public String getToken() throws TechnicalErrorException {
    AuthenticationRequest authenticationRequest =
        AuthenticationRequest.builder()
            .login(applicationProperties.getJwt().getUsername())
            .password(applicationProperties.getJwt().getPassword())
            .build();
    AuthenticationResponse authenticationResponse =
        webClient
            .post()
            .uri(applicationProperties.getJwt().getUrlAuth())
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(authenticationRequest)
            .retrieve()
            .bodyToMono(AuthenticationResponse.class)
            .doOnError(throwable -> log.error("Error while getting token", throwable))
            .block();

    if (authenticationResponse == null) {
      throw new TechnicalErrorException("Authentication failed");
    }

    return authenticationResponse.getAccessToken();
  }
}
