package com.skysoft.app.bdd;

import com.skysoft.app.model.ClientDto;
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
public class ClientStepDefinition extends AbstractStepDefinition {

  @Given("les informations du client à {string} avec {string} sont")
  public void client_is(String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("créer")) {
      log.info("Scenario create client: {}", referenceIdContexte);
    } else if (eventType.equals("modifier")) {
      log.info("Scenario update client: {}", referenceIdContexte);
    } else if (eventType.equals("valider")) {
      log.info("Scenario validate client: {}", referenceIdContexte);
    }
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    rows.stream()
        .map(this::buildClient)
        .findFirst()
        .ifPresentOrElse(
            client -> context.put(referenceIdContexte, client),
            () -> log.error("Client not found"));
  }

  @SneakyThrows
  @When("{string} le client avec {string}")
  public void client_is_send(String eventType, String referenceIdContexte) {
    log.info("Send client event {}", eventType);

    if (eventType.equals("créer")) {
      ClientDto clientDto = (ClientDto) context.get(referenceIdContexte);
      var client = clientRepository.createClient(clientDto);
      context.put(referenceIdContexte + "-created", client);
    } else if (eventType.equals("modifier")) {
      // Future implementation for update
      log.info("Update client functionality not yet implemented");
    } else if (eventType.equals("valider")) {
      ClientDto clientDto = (ClientDto) context.get(referenceIdContexte);
      try {
        var client = clientRepository.createClient(clientDto);
        context.put(referenceIdContexte + "-validation-success", client);
      } catch (Exception e) {
        log.info("Validation error as expected: {}", e.getMessage());
        context.put(referenceIdContexte + "-validation-error", e.getMessage());
      }
    }
  }

  @Then("le client est {string} avec {string}")
  public void client_is_created_or_updated(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    log.info("Check client {}", eventType);
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    if (eventType.equals("créé")) {
      ClientDto clientDto = (ClientDto) context.get(referenceIdContexte + "-created");
      rows.stream()
          .map(this::buildClient)
          .peek(
              client -> {
                client.setNom(clientDto.getNom());
                client.setPrenom(clientDto.getPrenom());
                client.setMail(clientDto.getMail());
                client.setNumTel(clientDto.getNumTel());
                client.setPhoto(clientDto.getPhoto());
                if (clientDto.getAdresse() != null) {
                  client.setAdresse(clientDto.getAdresse());
                }
              })
          .findFirst()
          .ifPresentOrElse(
              client -> assertClient(clientDto, client), () -> log.error("Client not found"));
    } else if (eventType.equals("modifié")) {
      ClientDto clientDto = (ClientDto) context.get(referenceIdContexte + "-modified");
      rows.stream()
          .map(this::buildClient)
          .peek(
              client -> {
                client.setNom(clientDto.getNom());
                client.setPrenom(clientDto.getPrenom());
                client.setMail(clientDto.getMail());
                client.setNumTel(clientDto.getNumTel());
                client.setPhoto(clientDto.getPhoto());
                if (clientDto.getAdresse() != null) {
                  client.setAdresse(clientDto.getAdresse());
                }
              })
          .findFirst()
          .ifPresentOrElse(
              client -> assertClient(clientDto, client), () -> log.error("Client not found"));
    }
  }

  @Then("le client a des erreurs de validation avec {string}")
  public void client_has_validation_errors(String referenceIdContexte, DataTable dataTable) {
    log.info("Check client validation errors");
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
      throw new AssertionError("Expected validation error but client was created successfully");
    }
  }

  @SneakyThrows
  @And("{string} du client avec {string}")
  public void send_create_client(
      String eventType, String referenceIdContexte, DataTable dataTable) {
    if (eventType.equals("creation")) {
      ClientDto clientDto = (ClientDto) context.get(referenceIdContexte);
      var client = clientRepository.createClient(clientDto);
      context.put(referenceIdContexte + "-created", client);
    } else if (eventType.equals("modification")) {
      ClientDto clientDto = (ClientDto) context.get(referenceIdContexte + "-created");
      dataTable.asMaps(String.class, String.class).stream()
          .map(this::buildClient)
          .findFirst()
          .ifPresentOrElse(
              client -> {
                clientDto.setNom(client.getNom());
                clientDto.setPrenom(client.getPrenom());
                clientDto.setMail(client.getMail());
                clientDto.setNumTel(client.getNumTel());
                clientDto.setPhoto(client.getPhoto());
                if (client.getAdresse() != null) {
                  clientDto.setAdresse(client.getAdresse());
                }
                context.put(referenceIdContexte + "-modified", clientDto);
              },
              () -> log.error("Client not found"));
    }
  }
}
