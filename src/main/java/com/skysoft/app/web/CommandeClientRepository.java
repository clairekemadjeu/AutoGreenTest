package com.skysoft.app.web;

import com.skysoft.app.exception.TechnicalErrorException;
import com.skysoft.app.model.CommandeClientDto;
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
public class CommandeClientRepository {
  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;
  private final AuthenticationRepository authenticationRepository;

  public CommandeClientDto createCommandeClient(CommandeClientDto commandeClientDto)
      throws TechnicalErrorException {
    return webClient
        .post()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/commandesclients")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .body(Mono.just(commandeClientDto), CommandeClientDto.class)
        .retrieve()
        .bodyToMono(CommandeClientDto.class)
        .doOnSuccess(
            commande ->
                log.info(
                    "CommandeClient: {}, {}",
                    commande.getId(),
                    "POST/commandesclients request successful"))
        .doOnError(throwable -> log.error("Error while creating commande client", throwable))
        .block();
  }
}
