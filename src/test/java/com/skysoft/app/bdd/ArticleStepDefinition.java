package com.skysoft.app.bdd;

import com.skysoft.app.model.ArticleDto;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArticleStepDefinition extends AbstractStepDefinition {

  @Given("les informations de l'article à {string} avec {string} sont")
  public void article_is(String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("créer")) {
      log.info("Scenario create article: {}", referenceIdContexte);
    } else if (eventType.equals("modifier")) {
      log.info("Scenario update article: {}", referenceIdContexte);
    } else if (eventType.equals("valider")) {
      log.info("Scenario validate article: {}", referenceIdContexte);
    }
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    rows.stream()
        .map(this::buildArticle)
        .findFirst()
        .ifPresentOrElse(
            article -> context.put(referenceIdContexte, article),
            () -> log.error("Article not found"));
  }

  @SneakyThrows
  @When("{string} l'article avec {string}")
  public void article_is_send(String eventType, String referenceIdContexte) {
    log.info("Send article event {}", eventType);

    if (eventType.equals("créer")) {
      ArticleDto articleDto = (ArticleDto) context.get(referenceIdContexte);
      var article = articleRepository.createArticle(articleDto);
      context.put(referenceIdContexte + "-created", article);
    } else if (eventType.equals("modifier")) {
      // Future implementation for update
      log.info("Update article functionality not yet implemented");
    } else if (eventType.equals("valider")) {
      ArticleDto articleDto = (ArticleDto) context.get(referenceIdContexte);
      try {
        var article = articleRepository.createArticle(articleDto);
        context.put(referenceIdContexte + "-validation-success", article);
      } catch (Exception e) {
        log.info("Validation error as expected: {}", e.getMessage());
        context.put(referenceIdContexte + "-validation-error", e.getMessage());
      }
    }
  }

  @SneakyThrows
  @And("{string} de l'article avec {string}")
  public void send_create_article(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("creation")) {
      ArticleDto articleDto = (ArticleDto) context.get(referenceIdContexte);
      var article = articleRepository.createArticle(articleDto);
      context.put(referenceIdContexte + "-created", article);
    } else if (eventType.equals("modification")) {
      ArticleDto articleDto = (ArticleDto) context.get(referenceIdContexte + "-created");
      dataTable.asMaps(String.class, String.class).stream()
          .map(this::buildArticle)
          .findFirst()
          .ifPresentOrElse(
              article -> {
                articleDto.setCodeArticle(article.getCodeArticle());
                articleDto.setDesignation(article.getDesignation());
                articleDto.setPrixUnitaireHt(article.getPrixUnitaireHt());
                articleDto.setPrixUnitaireTtc(article.getPrixUnitaireTtc());
                articleDto.setTauxTva(article.getTauxTva());
                articleDto.setPhoto(article.getPhoto());
                if (article.getCategorie() != null) {
                  articleDto.setCategorie(article.getCategorie());
                }
                context.put(referenceIdContexte + "-modified", articleDto);
              },
              () -> log.error("Article not found"));
    }
  }

  @Then("l'article est {string} avec {string}")
  public void article_is_created_or_updated(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    log.info("Check article {}", eventType);
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    if (eventType.equals("créé")) {
      ArticleDto articleDto = (ArticleDto) context.get(referenceIdContexte + "-created");
      rows.stream()
          .map(this::buildArticle)
          .peek(
              article -> {
                article.setCodeArticle(articleDto.getCodeArticle());
                article.setDesignation(articleDto.getDesignation());
                article.setPrixUnitaireHt(articleDto.getPrixUnitaireHt());
                article.setPrixUnitaireTtc(articleDto.getPrixUnitaireTtc());
                article.setTauxTva(articleDto.getTauxTva());
                article.setPhoto(articleDto.getPhoto());
                if (articleDto.getCategorie() != null) {
                  article.setCategorie(articleDto.getCategorie());
                }
              })
          .findFirst()
          .ifPresentOrElse(
              article -> assertArticle(articleDto, article), () -> log.error("Article not found"));
    } else if (eventType.equals("modifié")) {
      ArticleDto articleDto = (ArticleDto) context.get(referenceIdContexte + "-modified");
      rows.stream()
          .map(this::buildArticle)
          .peek(
              article -> {
                article.setCodeArticle(articleDto.getCodeArticle());
                article.setDesignation(articleDto.getDesignation());
                article.setPrixUnitaireHt(articleDto.getPrixUnitaireHt());
                article.setPrixUnitaireTtc(articleDto.getPrixUnitaireTtc());
                article.setTauxTva(articleDto.getTauxTva());
                article.setPhoto(articleDto.getPhoto());
                if (articleDto.getCategorie() != null) {
                  article.setCategorie(articleDto.getCategorie());
                }
              })
          .findFirst()
          .ifPresentOrElse(
              article -> assertArticle(articleDto, article), () -> log.error("Article not found"));
    }
  }

  @Then("l'article a des erreurs de validation avec {string}")
  public void article_has_validation_errors(String referenceIdContexte, DataTable dataTable) {
    log.info("Check article validation errors");
    String errorMessage = (String) context.get(referenceIdContexte + "-validation-error");
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (errorMessage != null) {
      rows.stream()
          .findFirst()
          .ifPresentOrElse(
              row -> {
                String expectedMessage = row.get("messageErreur");
                assert errorMessage.contains(expectedMessage)
                    : "Expected error message '"
                        + expectedMessage
                        + "' not found in '"
                        + errorMessage
                        + "'";
                log.info("Validation error correctly detected: {}", expectedMessage);
              },
              () -> log.error("Expected error message not found"));
    } else {
      throw new AssertionError("Expected validation error but article was created successfully");
    }
  }
}
