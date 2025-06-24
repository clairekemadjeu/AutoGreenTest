package com.skysoft.app.web;

import com.skysoft.app.exception.TechnicalErrorException;
import com.skysoft.app.model.MvtStkDto;
import com.skysoft.app.web.config.ApplicationProperties;
import java.math.BigDecimal;
import java.util.List;
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
public class MvtStkRepository {
  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;
  private final AuthenticationRepository authenticationRepository;

  public MvtStkDto entreeStock(MvtStkDto mvtStkDto) throws TechnicalErrorException {
    return webClient
        .post()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/mvtstk/entree")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .body(Mono.just(mvtStkDto), MvtStkDto.class)
        .retrieve()
        .bodyToMono(MvtStkDto.class)
        .doOnSuccess(
            mvt ->
                log.info(
                    "Entrée stock: {}, {}", mvt.getId(), "POST/mvtstk/entree request successful"))
        .doOnError(throwable -> log.error("Error while creating entrée stock", throwable))
        .block();
  }

  public MvtStkDto sortieStock(MvtStkDto mvtStkDto) throws TechnicalErrorException {
    return webClient
        .post()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/mvtstk/sortie")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .body(Mono.just(mvtStkDto), MvtStkDto.class)
        .retrieve()
        .bodyToMono(MvtStkDto.class)
        .doOnSuccess(
            mvt ->
                log.info(
                    "Sortie stock: {}, {}", mvt.getId(), "POST/mvtstk/sortie request successful"))
        .doOnError(throwable -> log.error("Error while creating sortie stock", throwable))
        .block();
  }

  public MvtStkDto correctionStockPos(MvtStkDto mvtStkDto) throws TechnicalErrorException {
    return webClient
        .post()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/mvtstk/correctionpos")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .body(Mono.just(mvtStkDto), MvtStkDto.class)
        .retrieve()
        .bodyToMono(MvtStkDto.class)
        .doOnSuccess(
            mvt ->
                log.info(
                    "Correction stock positive: {}, {}",
                    mvt.getId(),
                    "POST/mvtstk/correctionpos request successful"))
        .doOnError(
            throwable -> log.error("Error while creating correction stock positive", throwable))
        .block();
  }

  public MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto) throws TechnicalErrorException {
    return webClient
        .post()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/mvtstk/correctionneg")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .body(Mono.just(mvtStkDto), MvtStkDto.class)
        .retrieve()
        .bodyToMono(MvtStkDto.class)
        .doOnSuccess(
            mvt ->
                log.info(
                    "Correction stock négative: {}, {}",
                    mvt.getId(),
                    "POST/mvtstk/correctionneg request successful"))
        .doOnError(
            throwable -> log.error("Error while creating correction stock négative", throwable))
        .block();
  }

  public BigDecimal getStockReel(Long idArticle) throws TechnicalErrorException {
    return webClient
        .get()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/mvtstk/" + idArticle + "/stockreel")
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .retrieve()
        .bodyToMono(BigDecimal.class)
        .doOnSuccess(stock -> log.info("Stock réel article {}: {}", idArticle, stock))
        .doOnError(throwable -> log.error("Error while getting stock réel", throwable))
        .block();
  }

  public List<MvtStkDto> getMouvementsArticle(Long idArticle) throws TechnicalErrorException {
    return webClient
        .get()
        .uri(applicationProperties.getJwt().getUrlRoot() + "/mvtstk/" + idArticle + "/article")
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationRepository.getToken())
        .retrieve()
        .bodyToFlux(MvtStkDto.class)
        .collectList()
        .doOnSuccess(
            mouvements ->
                log.info(
                    "Mouvements article {}: {} mouvements trouvés", idArticle, mouvements.size()))
        .doOnError(throwable -> log.error("Error while getting mouvements article", throwable))
        .block();
  }
}
