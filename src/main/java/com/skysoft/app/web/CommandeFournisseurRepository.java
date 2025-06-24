package com.skysoft.app.web;

import com.skysoft.app.exception.TechnicalErrorException;
import com.skysoft.app.model.CommandeFournisseurDto;
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
public class CommandeFournisseurRepository {
  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;
  private final AuthenticationRepository authenticationRepository;

  public CommandeFournisseurDto createCommandeFournisseur(
      CommandeFournisseurDto commandeFournisseurDto) throws TechnicalErrorException {
    return webClient
        .post()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/commandesfournisseurs")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .body(Mono.just(commandeFournisseurDto), CommandeFournisseurDto.class)
        .retrieve()
        .bodyToMono(CommandeFournisseurDto.class)
        .doOnSuccess(
            commande ->
                log.info(
                    "CommandeFournisseur: {}, {}",
                    commande.getId(),
                    "POST/commandesfournisseurs request successful"))
        .doOnError(throwable -> log.error("Error while creating commande fournisseur", throwable))
        .block();
  }
}
