package com.skysoft.app.web;

import com.skysoft.app.model.EntrepriseDto;
import com.skysoft.app.web.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntrepriseRepository {
  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;

  public EntrepriseDto createEntreprise(EntrepriseDto entrepriseDto) {
    return webClient
        .post()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/entreprises")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .body(Mono.just(entrepriseDto), EntrepriseDto.class)
        .retrieve()
        .bodyToMono(EntrepriseDto.class)
        .doOnSuccess(
            entreprise ->
                log.info(
                    "Entreprise: {}, {}",
                    entreprise.getId(),
                    "POST/entreprises request successful"))
        .doOnError(throwable -> log.error("Error while creating entreprise", throwable))
        .block();
  }
}
