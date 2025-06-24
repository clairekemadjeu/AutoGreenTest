package com.skysoft.app.web;

import com.skysoft.app.exception.TechnicalErrorException;
import com.skysoft.app.model.UtilisateurDto;
import com.skysoft.app.web.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountManagerRepository {
  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;
  private final AuthenticationRepository authenticationRepository;

  public UtilisateurDto getAccount() throws TechnicalErrorException {
    return webClient
        .get()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/utilisateurs/account")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .retrieve()
        .bodyToMono(UtilisateurDto.class)
        .doOnSuccess(
            utilisateurDto ->
                log.info(
                    "Account: {}, {}",
                    utilisateurDto.getId(),
                    "GET/utilisateurs/account request successful"))
        .doOnError(throwable -> log.error("Error while getting account", throwable))
        .block();
  }
}
