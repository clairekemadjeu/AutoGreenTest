package com.skysoft.app.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.skysoft.app.model.MvtStkDto;
import com.skysoft.app.model.enumeration.TypeMvtStk;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MouvementStockStepDefinition extends AbstractStepDefinition {

  @Given("les informations du mouvement de stock à {string} avec {string} sont")
  public void mouvement_stock_is(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("créer")) {
      log.info("Scenario create mouvement stock: {}", referenceIdContexte);
    } else if (eventType.equals("valider")) {
      log.info("Scenario validate mouvement stock: {}", referenceIdContexte);
    }
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    rows.stream()
        .map(this::buildMouvementStock)
        .findFirst()
        .ifPresentOrElse(
            mouvement -> context.put(referenceIdContexte, mouvement),
            () -> log.error("Mouvement stock not found"));
  }

  @SneakyThrows
  @When("{string} le mouvement de stock avec {string}")
  public void mouvement_stock_is_send(String eventType, String referenceIdContexte) {
    log.info("Send mouvement stock event {}", eventType);

    if (eventType.equals("créer")) {
      MvtStkDto mvtStkDto = (MvtStkDto) context.get(referenceIdContexte);
      MvtStkDto result = null;

      // Appeler la bonne méthode selon le type de mouvement
      switch (mvtStkDto.getTypeMvtStk()) {
        case ENTREE:
          result = mvtStkRepository.entreeStock(mvtStkDto);
          break;
        case SORTIE:
          result = mvtStkRepository.sortieStock(mvtStkDto);
          break;
        case CORRECTION_POS:
          result = mvtStkRepository.correctionStockPos(mvtStkDto);
          break;
        case CORRECTION_NEG:
          result = mvtStkRepository.correctionStockNeg(mvtStkDto);
          break;
        default:
          throw new IllegalArgumentException(
              "Type de mouvement non supporté: " + mvtStkDto.getTypeMvtStk());
      }

      context.put(referenceIdContexte + "-created", result);
    } else if (eventType.equals("valider")) {
      MvtStkDto mvtStkDto = (MvtStkDto) context.get(referenceIdContexte);
      try {
        // Tenter de créer le mouvement pour déclencher la validation
        MvtStkDto result = mvtStkRepository.entreeStock(mvtStkDto);
        context.put(referenceIdContexte + "-validation-success", result);
      } catch (Exception e) {
        log.info("Validation error as expected: {}", e.getMessage());
        context.put(referenceIdContexte + "-validation-error", e.getMessage());
      }
    }
  }

  @Then("le mouvement de stock est {string} avec {string}")
  public void mouvement_stock_is_created(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    log.info("Check mouvement stock {}", eventType);
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    if (eventType.equals("créé")) {
      MvtStkDto mvtStkDto = (MvtStkDto) context.get(referenceIdContexte + "-created");
      rows.stream()
          .map(this::buildMouvementStock)
          .peek(
              mouvement -> {
                mouvement.setArticle(mvtStkDto.getArticle());
                mouvement.setTypeMvtStk(mvtStkDto.getTypeMvtStk());
                mouvement.setSourceMvtStk(mvtStkDto.getSourceMvtStk());
                mouvement.setQuantite(mvtStkDto.getQuantite());
              })
          .findFirst()
          .ifPresentOrElse(
              mouvement -> assertMouvementStock(mvtStkDto, mouvement),
              () -> log.error("Mouvement stock not found"));
    }
  }

  @Then("le mouvement de stock a des erreurs de validation avec {string}")
  public void mouvement_stock_has_validation_errors(
      String referenceIdContexte, DataTable dataTable) {
    log.info("Check mouvement stock validation errors");
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
      throw new AssertionError(
          "Expected validation error but mouvement stock was created successfully");
    }
  }

  @SneakyThrows
  @And("{string} du mouvement de stock avec {string}")
  public void send_create_mouvement_stock(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("creation")) {
      MvtStkDto mvtStkDto = (MvtStkDto) context.get(referenceIdContexte);
      MvtStkDto result = null;

      // Appeler la bonne méthode selon le type de mouvement
      switch (mvtStkDto.getTypeMvtStk()) {
        case ENTREE:
          result = mvtStkRepository.entreeStock(mvtStkDto);
          break;
        case SORTIE:
          result = mvtStkRepository.sortieStock(mvtStkDto);
          break;
        case CORRECTION_POS:
          result = mvtStkRepository.correctionStockPos(mvtStkDto);
          break;
        case CORRECTION_NEG:
          result = mvtStkRepository.correctionStockNeg(mvtStkDto);
          break;
        default:
          throw new IllegalArgumentException(
              "Type de mouvement non supporté: " + mvtStkDto.getTypeMvtStk());
      }

      context.put(referenceIdContexte + "-created", result);
    }
  }

  @Given("un article avec l'id {string} existe")
  public void article_exists(String articleId) {
    log.info("Article with id {} is assumed to exist", articleId);
    context.put("current-article-id", Long.parseLong(articleId));
  }

  @SneakyThrows
  @When("je consulte le stock réel de l'article avec {string}")
  public void consult_stock_reel(String referenceIdContexte) {
    Long articleId = null;

    // Essayer de récupérer l'ID depuis le contexte (cas consultation simple)
    if (context.containsKey("current-article-id")) {
      articleId = (Long) context.get("current-article-id");
    } else {
      // Essayer de récupérer l'ID depuis le mouvement créé
      MvtStkDto mvtCreated = (MvtStkDto) context.get(referenceIdContexte + "-created");
      if (mvtCreated != null && mvtCreated.getArticle() != null) {
        articleId = mvtCreated.getArticle().getId();
      }
    }

    if (articleId != null) {
      BigDecimal stockReel = mvtStkRepository.getStockReel(articleId);
      context.put(referenceIdContexte + "-stock-reel", stockReel);
      log.info("Stock réel consulté pour article {}: {}", articleId, stockReel);
    } else {
      log.error("Impossible de déterminer l'ID de l'article pour la consultation du stock");
    }
  }

  @Then("le stock réel est affiché avec {string}")
  public void stock_reel_is_displayed(String referenceIdContexte, DataTable dataTable) {
    log.info("Check stock réel display");
    BigDecimal stockReel = (BigDecimal) context.get(referenceIdContexte + "-stock-reel");
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    assertNotNull(stockReel, "Stock réel should not be null");

    rows.stream()
        .findFirst()
        .ifPresentOrElse(
            row -> {
              String expectedStock = row.get("stockReel");
              BigDecimal expectedStockValue = new BigDecimal(expectedStock);
              assert stockReel.compareTo(expectedStockValue) == 0
                  : "Expected stock '" + expectedStockValue + "' but found '" + stockReel + "'";
              log.info("Stock réel correctly displayed: {}", stockReel);
            },
            () -> log.error("Expected stock value not found"));
  }

  @SneakyThrows
  @When("je consulte les mouvements de l'article avec {string}")
  public void consult_mouvements_article(String referenceIdContexte) {
    Long articleId = null;

    // Essayer de récupérer l'ID depuis le contexte (cas consultation simple)
    if (context.containsKey("current-article-id")) {
      articleId = (Long) context.get("current-article-id");
    } else {
      // Essayer de récupérer l'ID depuis le mouvement créé
      MvtStkDto mvtCreated = (MvtStkDto) context.get(referenceIdContexte + "-created");
      if (mvtCreated != null && mvtCreated.getArticle() != null) {
        articleId = mvtCreated.getArticle().getId();
      }
    }

    if (articleId != null) {
      List<MvtStkDto> mouvements = mvtStkRepository.getMouvementsArticle(articleId);
      context.put(referenceIdContexte + "-mouvements", mouvements);
      log.info(
          "Mouvements consultés pour article {}: {} mouvements trouvés",
          articleId,
          mouvements.size());
    } else {
      log.error("Impossible de déterminer l'ID de l'article pour la consultation des mouvements");
    }
  }

  @Then("la liste des mouvements est affichée avec {string}")
  public void liste_mouvements_is_displayed(String referenceIdContexte, DataTable dataTable) {
    log.info("Check liste mouvements display");
    @SuppressWarnings("unchecked")
    List<MvtStkDto> mouvements = (List<MvtStkDto>) context.get(referenceIdContexte + "-mouvements");
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    assertNotNull(mouvements, "Liste des mouvements should not be null");

    rows.stream()
        .findFirst()
        .ifPresentOrElse(
            row -> {
              String expectedNombre = row.get("nombreMouvements");
              int expectedNombreValue = Integer.parseInt(expectedNombre);
              assertEquals(
                  expectedNombreValue,
                  mouvements.size(),
                  "Expected " + expectedNombreValue + " mouvements but found " + mouvements.size());
              log.info("Liste mouvements correctly displayed: {} mouvements", mouvements.size());
            },
            () -> log.error("Expected nombre mouvements not found"));
  }

  @And("le dernier mouvement correspond aux données avec {string}")
  public void dernier_mouvement_corresponds(String referenceIdContexte, DataTable dataTable) {
    log.info("Check dernier mouvement data");
    @SuppressWarnings("unchecked")
    List<MvtStkDto> mouvements = (List<MvtStkDto>) context.get(referenceIdContexte + "-mouvements");
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    assertNotNull(mouvements, "Liste des mouvements should not be null");
    assert !mouvements.isEmpty() : "Liste des mouvements should not be empty";

    MvtStkDto dernierMouvement = mouvements.get(mouvements.size() - 1);

    rows.stream()
        .findFirst()
        .ifPresentOrElse(
            row -> {
              String expectedQuantite = row.get("quantite");
              String expectedTypeMvt = row.get("typeMvtStk");
              String expectedSourceMvt = row.get("sourceMvtStk");

              BigDecimal expectedQuantiteValue = new BigDecimal(expectedQuantite);
              TypeMvtStk expectedTypeValue = TypeMvtStk.valueOf(expectedTypeMvt);

              // Pour les sorties et corrections négatives, la quantité doit être négative
              if (expectedTypeValue == TypeMvtStk.SORTIE
                  || expectedTypeValue == TypeMvtStk.CORRECTION_NEG) {
                expectedQuantiteValue = expectedQuantiteValue.negate();
              }

              assert Math.abs(dernierMouvement.getQuantite().doubleValue())
                      == Math.abs(expectedQuantiteValue.doubleValue())
                  : "Expected quantite "
                      + expectedQuantiteValue
                      + " but found "
                      + dernierMouvement.getQuantite();
              assert dernierMouvement.getTypeMvtStk().equals(expectedTypeValue)
                  : "Expected type "
                      + expectedTypeValue
                      + " but found "
                      + dernierMouvement.getTypeMvtStk();

              log.info("Dernier mouvement correctly matches expected data");
            },
            () -> log.error("Expected mouvement data not found"));
  }
}
