package com.skysoft.app.bdd;

import com.skysoft.app.model.CommandeClientDto;
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
public class CommandeClientStepDefinition extends AbstractStepDefinition {

  @Given("les informations de la commande client à {string} avec {string} sont")
  public void commande_client_is(String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("créer")) {
      log.info("Scenario create commande client: {}", referenceIdContexte);
    } else if (eventType.equals("modifier")) {
      log.info("Scenario update commande client: {}", referenceIdContexte);
    } else if (eventType.equals("valider")) {
      log.info("Scenario validate commande client: {}", referenceIdContexte);
    }
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    rows.stream()
        .map(this::buildCommandeClient)
        .findFirst()
        .ifPresentOrElse(
            commande -> context.put(referenceIdContexte, commande),
            () -> log.error("Commande client not found"));
  }

  @SneakyThrows
  @When("{string} la commande client avec {string}")
  public void commande_client_is_send(String eventType, String referenceIdContexte) {
    log.info("Send commande client event {}", eventType);

    if (eventType.equals("créer")) {
      CommandeClientDto commandeClientDto = (CommandeClientDto) context.get(referenceIdContexte);
      var commande = commandeClientRepository.createCommandeClient(commandeClientDto);
      context.put(referenceIdContexte + "-created", commande);
    } else if (eventType.equals("modifier")) {
      // Future implementation for update
      log.info("Update commande client functionality not yet implemented");
    } else if (eventType.equals("valider")) {
      CommandeClientDto commandeClientDto = (CommandeClientDto) context.get(referenceIdContexte);
      try {
        var commande = commandeClientRepository.createCommandeClient(commandeClientDto);
        context.put(referenceIdContexte + "-validation-success", commande);
      } catch (Exception e) {
        log.info("Validation error as expected: {}", e.getMessage());
        context.put(referenceIdContexte + "-validation-error", e.getMessage());
      }
    }
  }

  @Then("la commande client est {string} avec {string}")
  public void commande_client_is_created_or_updated(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    log.info("Check commande client {}", eventType);
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    if (eventType.equals("créée")) {
      CommandeClientDto commandeClientDto = (CommandeClientDto) context.get(referenceIdContexte + "-created");
      rows.stream()
          .map(this::buildCommandeClient)
          .findFirst()
          .ifPresentOrElse(
              commande -> assertCommandeClient(commandeClientDto, commande),
              () -> log.error("Commande client not found"));
    } else if (eventType.equals("modifiée")) {
      CommandeClientDto commandeClientDto = (CommandeClientDto) context.get(referenceIdContexte + "-modified");
      rows.stream()
          .map(this::buildCommandeClient)
          .findFirst()
          .ifPresentOrElse(
              commande -> assertCommandeClient(commandeClientDto, commande),
              () -> log.error("Commande client not found"));
    }
  }

  @Then("la commande client a des erreurs de validation avec {string}")
  public void commande_client_has_validation_errors(
      String referenceIdContexte, DataTable dataTable) {
    log.info("Check commande client validation errors");
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
      throw new AssertionError("Expected validation error but commande client was created successfully");
    }
  }

  @SneakyThrows
  @And("{string} de la commande client avec {string}")
  public void send_create_commande_client(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("creation")) {
      CommandeClientDto commandeClientDto = (CommandeClientDto) context.get(referenceIdContexte);
      var commande = commandeClientRepository.createCommandeClient(commandeClientDto);
      context.put(referenceIdContexte + "-created", commande);
    } else if (eventType.equals("modification")) {
      CommandeClientDto commandeClientDto = (CommandeClientDto) context.get(referenceIdContexte + "-created");
      dataTable.asMaps(String.class, String.class).stream()
          .map(this::buildCommandeClient)
          .findFirst()
          .ifPresentOrElse(
              commande -> {
                commandeClientDto.setCode(commande.getCode());
                commandeClientDto.setEtatCommande(commande.getEtatCommande());
                if (commande.getClient() != null) {
                  commandeClientDto.setClient(commande.getClient());
                }
                if (commande.getLigneCommandeClients() != null) {
                  commandeClientDto.setLigneCommandeClients(commande.getLigneCommandeClients());
                }
                context.put(referenceIdContexte + "-modified", commandeClientDto);
              },
              () -> log.error("Commande client not found"));
    }
  }
}
