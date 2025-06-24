package com.skysoft.app.bdd;

import com.skysoft.app.exception.TechnicalErrorException;
import com.skysoft.app.model.CategorieDto;
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
public class ParametreStepDefinition extends AbstractStepDefinition {

  @Given("les informations de la categorie à {string} avec {string} sont")
  public void category_is(String eventTye, String referenceIdContexte, DataTable dataTable) {
    if (eventTye.equals("créer")) {
      log.info("Scenario create category: {}", referenceIdContexte);
    } else if (eventTye.equals("modifier")) {
      log.info("Scenario update category: {}", referenceIdContexte);
    }
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    rows.stream()
        .map(this::buildCategorie)
        .findFirst()
        .ifPresentOrElse(
            categorie -> context.put(referenceIdContexte, categorie),
            () -> log.error("Categorie not found"));
  }

  @SneakyThrows
  @When("{string} la categorie avec {string}")
  public void category_is_send(String eventTye, String referenceIdContexte) {
    log.info("Send category event {}", eventTye);
    if (eventTye.equals("créer")) {
      CategorieDto categorieDto = (CategorieDto) context.get(referenceIdContexte);
      var categorie = categorieRepository.createCategorie(categorieDto);
      context.put(referenceIdContexte + "-created", categorie);
    } else if (eventTye.equals("modifier")) {
      // categorieRepository.updateCategorie((CategorieDto) context.get(referenceIdContexte));
    }
  }

  @Then("la categorie est {string} avec {string}")
  public void category_is_created_or_updated(
      String eventTye, String referenceIdContexte, DataTable dataTable) {
    log.info("Check category {}", eventTye);
    if (eventTye.equals("créée")) {
      CategorieDto categorieDto = (CategorieDto) context.get(referenceIdContexte + "-created");
      dataTable.asMaps(String.class, String.class).stream()
          .map(this::buildCategorie)
          .peek(
              categorie -> {
                categorie.setCode(categorieDto.getCode());
                categorie.setDesignation(categorieDto.getDesignation());
              })
          .findFirst()
          .ifPresentOrElse(
              categorie -> assertCategorie(categorieDto, categorie),
              () -> log.error("Categorie not found"));
    } else if (eventTye.equals("modifiée")) {
      CategorieDto categorieDto = (CategorieDto) context.get(referenceIdContexte + "-updated");
      dataTable.asMaps(String.class, String.class).stream()
          .map(this::buildCategorie)
          .findFirst()
          .ifPresentOrElse(
              categorie -> {
                categorie.setCode(categorieDto.getCode());
                categorie.setDesignation(categorieDto.getDesignation());
                assertCategorie(categorieDto, categorie);
              },
              () -> log.error("Categorie not found"));
    }
  }

  @SneakyThrows
  @And("{string} de la categorie avec {string}")
  public void send_create_category(
      String eventTye, String referenceIdContexte, DataTable dataTable) {
    if (eventTye.equals("creation")) {
      CategorieDto categorieDto = (CategorieDto) context.get(referenceIdContexte);
      var categorie = categorieRepository.createCategorie(categorieDto);
      context.put(referenceIdContexte + "-created", categorie);
    } else if (eventTye.equals("modification")) {
      CategorieDto categorieDto = (CategorieDto) context.get(referenceIdContexte + "-created");
      dataTable.asMaps(String.class, String.class).stream()
          .map(this::buildCategorie)
          .findFirst()
          .ifPresentOrElse(
              categorie -> {
                categorieDto.setCode(categorie.getCode());
                categorieDto.setDesignation(categorie.getDesignation());
                CategorieDto categorieUpdated = null;
                try {
                  categorieUpdated = categorieRepository.createCategorie(categorieDto);
                } catch (TechnicalErrorException e) {
                  throw new RuntimeException(e);
                }
                context.put(referenceIdContexte + "-updated", categorieUpdated);
              },
              () -> log.error("Categorie not found"));
    }
  }
}
