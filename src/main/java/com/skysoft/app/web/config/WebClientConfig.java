package com.skysoft.app.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebClientConfig {
  private final ObjectMapper objectMapper;

  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    HttpClient httpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
            .runOn(LoopResources.create("http-client-pool"))
            .doOnConnected(
                conn -> {
                  log.info("Connected to {}", conn.channel().remoteAddress());
                  conn.addHandlerLast(new ReadTimeoutHandler(10_000, TimeUnit.MILLISECONDS));
                  conn.addHandlerLast(new WriteTimeoutHandler(10_000, TimeUnit.MILLISECONDS));
                });
    return builder
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .exchangeStrategies(getExchangeStrategies())
        .defaultHeader("Content-Type", "application/json")
        .filter(logRequest())
        .filter(logResponse())
        .filter(handleErrors())
        .filters(
            f -> {
              f.add(logRequest());
              f.add(logResponse());
            })
        .build();
  }

  private ExchangeFilterFunction handleErrors() {
    return ExchangeFilterFunction.ofResponseProcessor(
        clientResponse -> {
          if (clientResponse.statusCode().is4xxClientError()
              || clientResponse.statusCode().is5xxServerError()) {
            return clientResponse
                .bodyToMono(String.class)
                .flatMap(
                    errorBody -> {
                      log.error("Error: {}", errorBody);
                      return Mono.error(
                          new RuntimeException(errorBody + " - " + clientResponse.statusCode()));
                    });
          }
          return Mono.just(clientResponse);
        });
  }

  private ExchangeFilterFunction logRequest() {
    return (clientRequest, next) -> {
      log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
      clientRequest
          .headers()
          .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
      return next.exchange(clientRequest);
    };
  }

  private ExchangeFilterFunction logResponse() {
    return (clientResponse, next) -> {
      clientResponse
          .headers()
          .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
      return next.exchange(clientResponse);
    };
  }

  private ExchangeStrategies getExchangeStrategies() {
    return ExchangeStrategies.builder()
        .codecs(
            configurer -> {
              configurer.defaultCodecs().enableLoggingRequestDetails(true);
              configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
              configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
            })
        .build();
  }
}
