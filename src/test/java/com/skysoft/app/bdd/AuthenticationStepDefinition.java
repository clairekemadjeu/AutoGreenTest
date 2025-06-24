package com.skysoft.app.bdd;

import com.skysoft.app.model.EntrepriseDto;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationStepDefinition extends AbstractStepDefinition {

  @Given("les informations de l'entreprise à {string} avec {string} sont")
  public void business_account_is(
      String eventTye, String referenceIdContexte, DataTable dataTable) {
    Long idEntreprise = null;
    if (eventTye.equals("créer")) {
      log.info("Scenario: {}", referenceIdContexte);
    } else if (eventTye.equals("modifier")) {
      idEntreprise = (Long) context.get(referenceIdContexte);
    }
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    rows.stream()
        .map(this::buildEnreprise)
        .findFirst()
        .ifPresentOrElse(
            entrepriseDto -> context.put(referenceIdContexte, entrepriseDto),
            () -> log.error("Entreprise not found"));
  }

  @SneakyThrows
  @When("{string} l'entreprise avec {string}")
  public void business_account_is_send(String eventTye, String referenceIdContexte) {
    log.info("Send business account event {}", eventTye);

    if (eventTye.equals("créer")) {
      EntrepriseDto entrepriseDto = (EntrepriseDto) context.get(referenceIdContexte);
      var entreprise = entrepriseRepository.createEntreprise(entrepriseDto);
      context.put(referenceIdContexte + "-created", entreprise);
    } else if (eventTye.equals("modifier")) {
      // entrepriseRepository.updateEntreprise((EntrepriseDto) context.get(referenceIdContexte));
    }
  }

  @Then("l'entreprise est {string} avec {string}")
  public void business_account_is_created_or_updated(
      String eventTye, String referenceIdContexte, DataTable dataTable) {
    log.info("Check business account {}", eventTye);
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    if (eventTye.equals("créée")) {
      EntrepriseDto entrepriseDto = (EntrepriseDto) context.get(referenceIdContexte + "-created");
      rows.stream()
          .map(this::buildEnreprise)
          .findFirst()
          .ifPresentOrElse(
              entreprise -> assertEntreprise(entrepriseDto, entreprise),
              () -> log.error("Entreprise not found"));
    } else if (eventTye.equals("modifiée")) {
      // check entreprise is updated
    }
  }
}
