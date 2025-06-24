package com.skysoft.app.web;

import com.skysoft.app.exception.TechnicalErrorException;
import com.skysoft.app.model.FournisseurDto;
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
public class FournisseurRepository {
  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;
  private final AuthenticationRepository authenticationRepository;

  public FournisseurDto createFournisseur(FournisseurDto fournisseurDto)
      throws TechnicalErrorException {
    return webClient
        .post()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/fournisseurs")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .body(Mono.just(fournisseurDto), FournisseurDto.class)
        .retrieve()
        .bodyToMono(FournisseurDto.class)
        .doOnSuccess(
            fournisseur ->
                log.info(
                    "Fournisseur: {}, {}",
                    fournisseur.getId(),
                    "POST/fournisseurs request successful"))
        .doOnError(throwable -> log.error("Error while creating fournisseur", throwable))
        .block();
  }
}
