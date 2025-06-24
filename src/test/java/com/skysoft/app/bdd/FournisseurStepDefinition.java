package com.skysoft.app.bdd;

import com.skysoft.app.model.FournisseurDto;
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
public class FournisseurStepDefinition extends AbstractStepDefinition {

  @Given("les informations du fournisseur à {string} avec {string} sont")
  public void fournisseur_is(String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("créer")) {
      log.info("Scenario create fournisseur: {}", referenceIdContexte);
    } else if (eventType.equals("modifier")) {
      log.info("Scenario update fournisseur: {}", referenceIdContexte);
    } else if (eventType.equals("valider")) {
      log.info("Scenario validate fournisseur: {}", referenceIdContexte);
    }
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    rows.stream()
        .map(this::buildFournisseur)
        .findFirst()
        .ifPresentOrElse(
            fournisseur -> context.put(referenceIdContexte, fournisseur),
            () -> log.error("Fournisseur not found"));
  }

  @SneakyThrows
  @When("{string} le fournisseur avec {string}")
  public void fournisseur_is_send(String eventType, String referenceIdContexte) {
    log.info("Send fournisseur event {}", eventType);

    if (eventType.equals("créer")) {
      FournisseurDto fournisseurDto = (FournisseurDto) context.get(referenceIdContexte);
      var fournisseur = fournisseurRepository.createFournisseur(fournisseurDto);
      context.put(referenceIdContexte + "-created", fournisseur);
    } else if (eventType.equals("modifier")) {
      // Future implementation for update
      log.info("Update fournisseur functionality not yet implemented");
    } else if (eventType.equals("valider")) {
      FournisseurDto fournisseurDto = (FournisseurDto) context.get(referenceIdContexte);
      try {
        var fournisseur = fournisseurRepository.createFournisseur(fournisseurDto);
        context.put(referenceIdContexte + "-validation-success", fournisseur);
      } catch (Exception e) {
        log.info("Validation error as expected: {}", e.getMessage());
        context.put(referenceIdContexte + "-validation-error", e.getMessage());
      }
    }
  }

  @Then("le fournisseur est {string} avec {string}")
  public void fournisseur_is_created_or_updated(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    log.info("Check fournisseur {}", eventType);
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    if (eventType.equals("créé")) {
      FournisseurDto fournisseurDto =
          (FournisseurDto) context.get(referenceIdContexte + "-created");
      rows.stream()
          .map(this::buildFournisseur)
          .peek(
              fournisseur -> {
                fournisseur.setNom(fournisseurDto.getNom());
                fournisseur.setPrenom(fournisseurDto.getPrenom());
                fournisseur.setMail(fournisseurDto.getMail());
                fournisseur.setNumTel(fournisseurDto.getNumTel());
                fournisseur.setPhoto(fournisseurDto.getPhoto());
                if (fournisseurDto.getAdresse() != null) {
                  fournisseur.setAdresse(fournisseurDto.getAdresse());
                }
              })
          .findFirst()
          .ifPresentOrElse(
              fournisseur -> assertFournisseur(fournisseurDto, fournisseur),
              () -> log.error("Fournisseur not found"));
    } else if (eventType.equals("modifié")) {
      FournisseurDto fournisseurDto =
          (FournisseurDto) context.get(referenceIdContexte + "-modified");
      rows.stream()
          .map(this::buildFournisseur)
          .peek(
              fournisseur -> {
                fournisseur.setNom(fournisseurDto.getNom());
                fournisseur.setPrenom(fournisseurDto.getPrenom());
                fournisseur.setMail(fournisseurDto.getMail());
                fournisseur.setNumTel(fournisseurDto.getNumTel());
                fournisseur.setPhoto(fournisseurDto.getPhoto());
                if (fournisseurDto.getAdresse() != null) {
                  fournisseur.setAdresse(fournisseurDto.getAdresse());
                }
              })
          .findFirst()
          .ifPresentOrElse(
              fournisseur -> assertFournisseur(fournisseurDto, fournisseur),
              () -> log.error("Fournisseur not found"));
    }
  }

  @Then("le fournisseur a des erreurs de validation avec {string}")
  public void fournisseur_has_validation_errors(String referenceIdContexte, DataTable dataTable) {
    log.info("Check fournisseur validation errors");
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
          "Expected validation error but fournisseur was created successfully");
    }
  }

  @SneakyThrows
  @And("{string} du fournisseur avec {string}")
  public void send_create_fournisseur(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("creation")) {
      FournisseurDto fournisseurDto = (FournisseurDto) context.get(referenceIdContexte);
      var fournisseur = fournisseurRepository.createFournisseur(fournisseurDto);
      context.put(referenceIdContexte + "-created", fournisseur);
    } else if (eventType.equals("modification")) {
      FournisseurDto fournisseurDto =
          (FournisseurDto) context.get(referenceIdContexte + "-created");
      dataTable.asMaps(String.class, String.class).stream()
          .map(this::buildFournisseur)
          .findFirst()
          .ifPresentOrElse(
              fournisseur -> {
                fournisseurDto.setNom(fournisseur.getNom());
                fournisseurDto.setPrenom(fournisseur.getPrenom());
                fournisseurDto.setMail(fournisseur.getMail());
                fournisseurDto.setNumTel(fournisseur.getNumTel());
                fournisseurDto.setPhoto(fournisseur.getPhoto());
                if (fournisseur.getAdresse() != null) {
                  fournisseurDto.setAdresse(fournisseur.getAdresse());
                }
                context.put(referenceIdContexte + "-modified", fournisseurDto);
              },
              () -> log.error("Fournisseur not found"));
    }
  }
}
