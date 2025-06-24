package com.skysoft.app.bdd;

import com.skysoft.app.model.CommandeFournisseurDto;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class CommandeFournisseurStepDefinition extends AbstractStepDefinition {

  @Given("les informations de la commande fournisseur à {string} avec {string} sont")
  public void commande_fournisseur_is(String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("créer")) {
      log.info("Scenario create commande fournisseur: {}", referenceIdContexte);
    } else if (eventType.equals("modifier")) {
      log.info("Scenario update commande fournisseur: {}", referenceIdContexte);
    } else if (eventType.equals("valider")) {
      log.info("Scenario validate commande fournisseur: {}", referenceIdContexte);
    }
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    rows.stream()
        .map(this::buildCommandeFournisseur)
        .findFirst()
        .ifPresentOrElse(
            commande -> context.put(referenceIdContexte, commande),
            () -> log.error("Commande fournisseur not found"));
  }

  @And("les données d'adresse du fournisseur avec {string} sont")
  public void fournisseur_address_data(String referenceIdContexte, DataTable dataTable) {
    log.info("Adding supplier address data for: {}", referenceIdContexte);
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    // Get the existing commande fournisseur from context
    CommandeFournisseurDto commandeFournisseur = (CommandeFournisseurDto) context.get(referenceIdContexte);

    if (commandeFournisseur != null && commandeFournisseur.getFournisseur() != null) {
      rows.stream()
          .findFirst()
          .ifPresent(row -> {
            // Build address and set it to the supplier
            var adresse = buildAdresseForFournisseurCommande(row);
            commandeFournisseur.getFournisseur().setAdresse(adresse);
            // Update the context with the modified commande
            context.put(referenceIdContexte, commandeFournisseur);
            log.info("Supplier address data added successfully");
          });
    } else {
      log.error("Commande fournisseur or supplier not found in context for: {}", referenceIdContexte);
    }
  }

  @SneakyThrows
  @When("{string} la commande fournisseur avec {string}")
  public void commande_fournisseur_is_send(String eventType, String referenceIdContexte) {
    log.info("Send commande fournisseur event {}", eventType);

    if (eventType.equals("créer")) {
      CommandeFournisseurDto commandeFournisseurDto = (CommandeFournisseurDto) context.get(referenceIdContexte);
      var commande = commandeFournisseurRepository.createCommandeFournisseur(commandeFournisseurDto);
      context.put(referenceIdContexte + "-created", commande);
    } else if (eventType.equals("modifier")) {
      // Future implementation for update
      log.info("Update commande fournisseur functionality not yet implemented");
    } else if (eventType.equals("valider")) {
      CommandeFournisseurDto commandeFournisseurDto = (CommandeFournisseurDto) context.get(referenceIdContexte);
      try {
        var commande = commandeFournisseurRepository.createCommandeFournisseur(commandeFournisseurDto);
        context.put(referenceIdContexte + "-validation-success", commande);
      } catch (Exception e) {
        log.info("Validation error as expected: {}", e.getMessage());
        context.put(referenceIdContexte + "-validation-error", e.getMessage());
      }
    }
  }

  @Then("la commande fournisseur est {string} avec {string}")
  public void commande_fournisseur_is_created_or_updated(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    log.info("Check commande fournisseur {}", eventType);
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    if (eventType.equals("créée")) {
      CommandeFournisseurDto commandeFournisseurDto = (CommandeFournisseurDto) context.get(referenceIdContexte + "-created");
      rows.stream()
          .map(this::buildCommandeFournisseur)
          .findFirst()
          .ifPresentOrElse(
              commande -> assertCommandeFournisseur(commandeFournisseurDto, commande),
              () -> log.error("Commande fournisseur not found"));
    } else if (eventType.equals("modifiée")) {
      CommandeFournisseurDto commandeFournisseurDto = (CommandeFournisseurDto) context.get(referenceIdContexte + "-modified");
      rows.stream()
          .map(this::buildCommandeFournisseur)
          .findFirst()
          .ifPresentOrElse(
              commande -> assertCommandeFournisseur(commandeFournisseurDto, commande),
              () -> log.error("Commande fournisseur not found"));
    }
  }

  @Then("la commande fournisseur a des erreurs de validation avec {string}")
  public void commande_fournisseur_has_validation_errors(
      String referenceIdContexte, DataTable dataTable) {
    log.info("Check commande fournisseur validation errors");
    String errorMessage = (String) context.get(referenceIdContexte + "-validation-error");
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (errorMessage != null) {
      rows.stream()
          .findFirst()
          .ifPresentOrElse(
              row -> {
                String expectedMessage = row.get("messageErreur");
                assert errorMessage.contains(expectedMessage) :
                    "Expected error message '" + expectedMessage + "' not found in '" + errorMessage + "'";
                log.info("Validation error correctly detected: {}", expectedMessage);
              },
              () -> log.error("Expected error message not found"));
    } else {
      throw new AssertionError("Expected validation error but commande fournisseur was created successfully");
    }
  }

  @SneakyThrows
  @And("{string} de la commande fournisseur avec {string}")
  public void send_create_commande_fournisseur(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("creation")) {
      CommandeFournisseurDto commandeFournisseurDto = (CommandeFournisseurDto) context.get(referenceIdContexte);
      var commande = commandeFournisseurRepository.createCommandeFournisseur(commandeFournisseurDto);
      context.put(referenceIdContexte + "-created", commande);
    } else if (eventType.equals("modification")) {
      CommandeFournisseurDto commandeFournisseurDto = (CommandeFournisseurDto) context.get(referenceIdContexte + "-created");
      dataTable.asMaps(String.class, String.class).stream()
          .map(this::buildCommandeFournisseur)
          .findFirst()
          .ifPresentOrElse(
              commande -> {
                commandeFournisseurDto.setCode(commande.getCode());
                commandeFournisseurDto.setEtatCommande(commande.getEtatCommande());
                if (commande.getFournisseur() != null) {
                  commandeFournisseurDto.setFournisseur(commande.getFournisseur());
                }
                if (commande.getLigneCommandeFournisseurs() != null) {
                  commandeFournisseurDto.setLigneCommandeFournisseurs(commande.getLigneCommandeFournisseurs());
                }
                context.put(referenceIdContexte + "-modified", commandeFournisseurDto);
              },
              () -> log.error("Commande fournisseur not found"));
    }
  }
}
