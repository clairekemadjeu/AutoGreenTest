package com.skysoft.app.bdd;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.skysoft.app.AutoGreenTestApplication;
import com.skysoft.app.model.AdresseDto;
import com.skysoft.app.model.ArticleDto;
import com.skysoft.app.model.CategorieDto;
import com.skysoft.app.model.ClientDto;
import com.skysoft.app.model.CommandeClientDto;
import com.skysoft.app.model.CommandeFournisseurDto;
import com.skysoft.app.model.EntrepriseDto;
import com.skysoft.app.model.FournisseurDto;
import com.skysoft.app.model.LigneCommandeClientDto;
import com.skysoft.app.model.LigneCommandeFournisseurDto;
import com.skysoft.app.model.LigneVenteDto;
import com.skysoft.app.model.MvtStkDto;
import com.skysoft.app.model.VenteDto;
import com.skysoft.app.model.enumeration.EtatCommande;
import com.skysoft.app.model.enumeration.SourceMvtStk;
import com.skysoft.app.model.enumeration.TypeMvtStk;
import com.skysoft.app.web.AccountManagerRepository;
import com.skysoft.app.web.ArticleRepository;
import com.skysoft.app.web.AuthenticationRepository;
import com.skysoft.app.web.CategorieRepository;
import com.skysoft.app.web.ClientRepository;
import com.skysoft.app.web.CommandeClientRepository;
import com.skysoft.app.web.CommandeFournisseurRepository;
import com.skysoft.app.web.EntrepriseRepository;
import com.skysoft.app.web.FournisseurRepository;
import com.skysoft.app.web.MvtStkRepository;
import com.skysoft.app.web.VenteRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;

@CucumberContextConfiguration
@SpringBootTest(
    classes = AutoGreenTestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
@AutoConfigureMockMvc
@Slf4j
public class AbstractStepDefinition extends ExecutionContext {
  @Autowired protected AccountManagerRepository accountManagerRepository;
  @Autowired protected AuthenticationRepository authenticationRepository;
  @Autowired protected EntrepriseRepository entrepriseRepository;
  @Autowired protected CategorieRepository categorieRepository;
  @Autowired protected ArticleRepository articleRepository;
  @Autowired protected ClientRepository clientRepository;
  @Autowired protected FournisseurRepository fournisseurRepository;
  @Autowired protected CommandeClientRepository commandeClientRepository;
  @Autowired protected CommandeFournisseurRepository commandeFournisseurRepository;
  @Autowired protected VenteRepository venteRepository;
  @Autowired protected MvtStkRepository mvtStkRepository;
  @Autowired protected MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected WebClient webClient;

  protected EntrepriseDto buildEnreprise(Map<String, String> row) {
    return EntrepriseDto.builder()
        .nom(row.get("nom"))
        .description(row.get("description"))
        .email(row.get("email"))
        .password(row.get("password"))
        .codeFiscal(row.get("codeFiscal"))
        .adresse(
            AdresseDto.builder()
                .adresse1(row.get("adresse1"))
                .adresse2(row.get("adresse2"))
                .ville(row.get("ville"))
                .pays(row.get("pays"))
                .codePostale(row.get("codePostale"))
                .build())
        .numTel(row.get("numTel"))
        .siteWeb(row.get("siteWeb"))
        .build();
  }

  protected void assertEntreprise(EntrepriseDto entrepriseDto, EntrepriseDto entreprise) {
    assert entrepriseDto.getNom().equals(entreprise.getNom());
    assert entrepriseDto.getDescription().equals(entreprise.getDescription());
    assert entrepriseDto.getEmail().equals(entreprise.getEmail());
    assert entrepriseDto.getCodeFiscal().equals(entreprise.getCodeFiscal());
    assert entrepriseDto.getAdresse().getAdresse1().equals(entreprise.getAdresse().getAdresse1());
    assert entrepriseDto.getAdresse().getAdresse2().equals(entreprise.getAdresse().getAdresse2());
    assert entrepriseDto.getAdresse().getVille().equals(entreprise.getAdresse().getVille());
    assert entrepriseDto.getAdresse().getPays().equals(entreprise.getAdresse().getPays());
    assert entrepriseDto
        .getAdresse()
        .getCodePostale()
        .equals(entreprise.getAdresse().getCodePostale());
    assert entrepriseDto.getNumTel().equals(entreprise.getNumTel());
    assert entrepriseDto.getSiteWeb().equals(entreprise.getSiteWeb());
  }

  protected CategorieDto buildCategorie(Map<String, String> row) {
    var fake = new Faker();
    return CategorieDto.builder()
        .code(resolveValue(row, "code", fake.bothify("CAT###")))
        .designation(resolveValue(row, "designation", fake.bothify("Catégorie de l'entreprise")))
        .build();
  }

  private String resolveValue(Map<String, String> row, String code, String randomValue) {
    return row.get(code) != null && row.get(code).equalsIgnoreCase("random")
        ? randomValue
        : row.get(code);
  }

  private String resolveValueWithEmpty(Map<String, String> row, String code, String randomValue) {
    String value = row.get(code);
    if (value == null || value.isEmpty()) {
      return null;
    }
    return value.equalsIgnoreCase("random") ? randomValue : value;
  }

  protected void assertCategorie(CategorieDto categorieCreated, CategorieDto categorie) {
    assert categorieCreated.getCode().equals(categorie.getCode());
    assert categorieCreated.getDesignation().equals(categorie.getDesignation());
    assertNotNull(categorieCreated.getIdEntreprise());
  }

  protected ArticleDto buildArticle(Map<String, String> row) {
    var fake = new Faker();
    return ArticleDto.builder()
        .codeArticle(resolveValue(row, "codeArticle", fake.bothify("ART###")))
        .designation(resolveValue(row, "designation", fake.commerce().productName()))
        .prixUnitaireHt(
            resolveBigDecimalValue(row, "prixUnitaireHt", fake.number().randomDouble(2, 10, 1000)))
        .prixUnitaireTtc(
            resolveBigDecimalValue(row, "prixUnitaireTtc", fake.number().randomDouble(2, 12, 1200)))
        .tauxTva(resolveBigDecimalValue(row, "tauxTva", 19.25))
        .photo(resolveValue(row, "photo", fake.internet().url()))
        .categorie(buildCategorieForArticle(row))
        .build();
  }

  private CategorieDto buildCategorieForArticle(Map<String, String> row) {
    var fake = new Faker();
    return CategorieDto.builder()
        .code(resolveValue(row, "categorieCode", fake.bothify("CAT###")))
        .designation(
            resolveValue(row, "categorieDesignation", fake.bothify("Catégorie de l'entreprise")))
        .build();
  }

  private BigDecimal resolveBigDecimalValue(
      Map<String, String> row, String key, double randomValue) {
    String value = row.get(key);
    if (value != null && value.equalsIgnoreCase("random")) {
      return BigDecimal.valueOf(randomValue);
    } else if (value != null) {
      return new BigDecimal(value);
    }
    return BigDecimal.valueOf(randomValue);
  }

  private BigDecimal resolveBigDecimalValueWithEmpty(
      Map<String, String> row, String key, double randomValue) {
    String value = row.get(key);
    if (value == null || value.isEmpty()) {
      return null;
    }
    if (value.equalsIgnoreCase("random")) {
      return BigDecimal.valueOf(randomValue);
    }
    return new BigDecimal(value);
  }

  protected void assertArticle(ArticleDto articleCreated, ArticleDto article) {
    assert articleCreated.getCodeArticle().equals(article.getCodeArticle());
    assert articleCreated.getDesignation().equals(article.getDesignation());
    assert articleCreated.getPrixUnitaireHt().compareTo(article.getPrixUnitaireHt()) == 0;
    assert articleCreated.getPrixUnitaireTtc().compareTo(article.getPrixUnitaireTtc()) == 0;
    assert articleCreated.getTauxTva().compareTo(article.getTauxTva()) == 0;
    if (article.getCategorie() != null) {
      assertNotNull(articleCreated.getCategorie());
      assert articleCreated.getCategorie().getCode().equals(article.getCategorie().getCode());
    }
  }

  protected ClientDto buildClient(Map<String, String> row) {
    var fake = new Faker();
    return ClientDto.builder()
        .nom(resolveValueWithEmpty(row, "nom", fake.name().lastName()))
        .prenom(resolveValueWithEmpty(row, "prenom", fake.name().firstName()))
        .mail(resolveValueWithEmpty(row, "mail", fake.internet().emailAddress()))
        .numTel(resolveValueWithEmpty(row, "numTel", fake.phoneNumber().phoneNumber()))
        .photo(resolveValueWithEmpty(row, "photo", fake.internet().url()))
        .adresse(buildAdresseForClient(row))
        .build();
  }

  private AdresseDto buildAdresseForClient(Map<String, String> row) {
    var fake = new Faker();
    return AdresseDto.builder()
        .adresse1(resolveValue(row, "adresse1", fake.address().streetAddress()))
        .adresse2(resolveValueWithEmpty(row, "adresse2", fake.address().secondaryAddress()))
        .ville(resolveValue(row, "ville", fake.address().city()))
        .pays(resolveValue(row, "pays", fake.address().country()))
        .codePostale(resolveValue(row, "codePostale", fake.address().zipCode()))
        .build();
  }

  private AdresseDto buildAdresseForClientCommande(Map<String, String> row) {
    var fake = new Faker();
    return AdresseDto.builder()
        .adresse1(resolveValue(row, "adresse1", fake.address().streetAddress()))
        .adresse2(resolveValueWithEmpty(row, "adresse2", fake.address().secondaryAddress()))
        .ville(resolveValue(row, "ville", fake.address().city()))
        .pays(resolveValue(row, "pays", fake.address().country()))
        .codePostale(resolveValue(row, "codePostale", fake.address().zipCode()))
        .build();
  }

  protected void assertClient(ClientDto clientCreated, ClientDto client) {
    assert clientCreated.getNom().equals(client.getNom());
    assert clientCreated.getPrenom().equals(client.getPrenom());
    assert clientCreated.getMail().equals(client.getMail());
    assert clientCreated.getNumTel().equals(client.getNumTel());
    if (client.getAdresse() != null) {
      assertNotNull(clientCreated.getAdresse());
      assert clientCreated.getAdresse().getAdresse1().equals(client.getAdresse().getAdresse1());
      assert clientCreated.getAdresse().getVille().equals(client.getAdresse().getVille());
    }
  }

  protected FournisseurDto buildFournisseur(Map<String, String> row) {
    var fake = new Faker();
    return FournisseurDto.builder()
        .nom(resolveValueWithEmpty(row, "nom", fake.name().lastName()))
        .prenom(resolveValueWithEmpty(row, "prenom", fake.name().firstName()))
        .mail(resolveValueWithEmpty(row, "mail", fake.internet().emailAddress()))
        .numTel(resolveValueWithEmpty(row, "numTel", fake.phoneNumber().phoneNumber()))
        .photo(resolveValueWithEmpty(row, "photo", fake.internet().url()))
        .adresse(buildAdresseForFournisseur(row))
        .build();
  }

  private AdresseDto buildAdresseForFournisseur(Map<String, String> row) {
    var fake = new Faker();
    return AdresseDto.builder()
        .adresse1(resolveValueWithEmpty(row, "adresse1", fake.address().streetAddress()))
        .adresse2(resolveValueWithEmpty(row, "adresse2", fake.address().secondaryAddress()))
        .ville(resolveValueWithEmpty(row, "ville", fake.address().city()))
        .pays(resolveValueWithEmpty(row, "pays", fake.address().country()))
        .codePostale(resolveValueWithEmpty(row, "codePostale", fake.address().zipCode()))
        .build();
  }

  protected AdresseDto buildAdresseForFournisseurCommande(Map<String, String> row) {
    var fake = new Faker();
    return AdresseDto.builder()
        .adresse1(resolveValue(row, "adresse1", fake.address().streetAddress()))
        .adresse2(resolveValueWithEmpty(row, "adresse2", fake.address().secondaryAddress()))
        .ville(resolveValue(row, "ville", fake.address().city()))
        .pays(resolveValue(row, "pays", fake.address().country()))
        .codePostale(resolveValue(row, "codePostale", fake.address().zipCode()))
        .build();
  }

  protected void assertFournisseur(FournisseurDto fournisseurCreated, FournisseurDto fournisseur) {
    assert fournisseurCreated.getNom().equals(fournisseur.getNom());
    assert fournisseurCreated.getPrenom().equals(fournisseur.getPrenom());
    assert fournisseurCreated.getMail().equals(fournisseur.getMail());
    assert fournisseurCreated.getNumTel().equals(fournisseur.getNumTel());
    if (fournisseur.getAdresse() != null) {
      assertNotNull(fournisseurCreated.getAdresse());
      assert fournisseurCreated
          .getAdresse()
          .getAdresse1()
          .equals(fournisseur.getAdresse().getAdresse1());
      assert fournisseurCreated.getAdresse().getVille().equals(fournisseur.getAdresse().getVille());
    }
  }

  protected CommandeClientDto buildCommandeClient(Map<String, String> row) {
    var fake = new Faker();
    return CommandeClientDto.builder()
        .code(resolveValueWithEmpty(row, "code", fake.bothify("CMD###")))
        .dateCommande(Instant.now())
        .etatCommande(resolveEtatCommande(row, "etatCommande", EtatCommande.EN_PREPARATION))
        .client(buildClientForCommande(row))
        .ligneCommandeClients(buildLignesCommandeClient(row))
        .build();
  }

  private ClientDto buildClientForCommande(Map<String, String> row) {
    var fake = new Faker();

    // Create a client first to get a valid ID
    ClientDto clientToCreate =
        ClientDto.builder()
            .id(resolveLongValue(row, "clientId", fake.number().randomNumber()))
            .nom(resolveValue(row, "clientNom", fake.name().lastName()))
            .prenom(resolveValue(row, "clientPrenom", fake.name().firstName()))
            .mail(resolveValue(row, "clientMail", fake.internet().emailAddress()))
            .numTel(resolveValueWithEmpty(row, "clientNumTel", fake.phoneNumber().phoneNumber()))
            .adresse(buildAdresseForClientCommande(row))
            .build();

    try {
      // Create the client and get the ID
      ClientDto createdClient = clientRepository.createClient(clientToCreate);
      return createdClient;
    } catch (Exception e) {
      log.error("Failed to create client for order: {}", e.getMessage());
      // Fallback: return client with a fake ID for testing
      clientToCreate.setId(fake.number().randomNumber());
      return clientToCreate;
    }
  }

  private List<LigneCommandeClientDto> buildLignesCommandeClient(Map<String, String> row) {
    List<LigneCommandeClientDto> lignes = new ArrayList<>();

    // Créer une ligne de commande avec un article
    LigneCommandeClientDto ligne =
        LigneCommandeClientDto.builder()
            .article(buildArticleForCommande(row))
            .quantite(resolveBigDecimalValueWithEmpty(row, "quantite", 1.0))
            .prixUnitaire(resolveBigDecimalValueWithEmpty(row, "prixUnitaire", 100.0))
            .build();

    lignes.add(ligne);
    return lignes;
  }

  private ArticleDto buildArticleForCommande(Map<String, String> row) {
    var fake = new Faker();
    return ArticleDto.builder()
        .codeArticle(resolveValueWithEmpty(row, "articleCode", fake.bothify("ART###")))
        .designation(
            resolveValueWithEmpty(row, "articleDesignation", fake.commerce().productName()))
        .prixUnitaireHt(resolveBigDecimalValueWithEmpty(row, "articlePrixHt", 100.0))
        .build();
  }

  private EtatCommande resolveEtatCommande(
      Map<String, String> row, String key, EtatCommande defaultValue) {
    String value = resolveValueWithEmpty(row, key, null);
    if (value == null || value.isEmpty()) {
      return defaultValue;
    }
    try {
      return EtatCommande.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return defaultValue;
    }
  }

  protected void assertCommandeClient(
      CommandeClientDto commandeCreated, CommandeClientDto commande) {
    assert commandeCreated.getCode().equals(commande.getCode());
    assert commandeCreated.getEtatCommande().equals(commande.getEtatCommande());
    assertNotNull(commandeCreated.getIdEntreprise());
    if (commande.getClient() != null) {
      assertNotNull(commandeCreated.getClient());
      assert commandeCreated.getClient().getNom().equals(commande.getClient().getNom());
    }
    if (commande.getLigneCommandeClients() != null
        && !commande.getLigneCommandeClients().isEmpty()) {
      assertNotNull(commandeCreated.getLigneCommandeClients());
      assert !commandeCreated.getLigneCommandeClients().isEmpty();
    }
  }

  protected CommandeFournisseurDto buildCommandeFournisseur(Map<String, String> row) {
    var fake = new Faker();
    return CommandeFournisseurDto.builder()
        .code(resolveValueWithEmpty(row, "code", fake.bothify("CMDF###")))
        .dateCommande(Instant.now())
        .etatCommande(resolveEtatCommande(row, "etatCommande", EtatCommande.EN_PREPARATION))
        .fournisseur(buildFournisseurForCommande(row))
        .ligneCommandeFournisseurs(buildLignesCommandeFournisseur(row))
        .build();
  }

  private FournisseurDto buildFournisseurForCommande(Map<String, String> row) {
    var fake = new Faker();
    return FournisseurDto.builder()
        .id(resolveLongValue(row, "fournisseurId", fake.number().randomNumber()))
        .nom(resolveValue(row, "fournisseurNom", fake.name().lastName()))
        .prenom(resolveValue(row, "fournisseurPrenom", fake.name().firstName()))
        .mail(resolveValue(row, "fournisseurMail", fake.internet().emailAddress()))
        .numTel(resolveValueWithEmpty(row, "fournisseurNumTel", fake.phoneNumber().phoneNumber()))
        .build();
  }

  private List<LigneCommandeFournisseurDto> buildLignesCommandeFournisseur(
      Map<String, String> row) {
    List<LigneCommandeFournisseurDto> lignes = new ArrayList<>();

    // Créer une ligne de commande avec un article
    LigneCommandeFournisseurDto ligne =
        LigneCommandeFournisseurDto.builder()
            .article(buildArticleForCommande(row))
            .quantite(resolveBigDecimalValueWithEmpty(row, "quantite", 1.0))
            .prixUnitaire(resolveBigDecimalValueWithEmpty(row, "prixUnitaire", 100.0))
            .build();

    lignes.add(ligne);
    return lignes;
  }

  protected void assertCommandeFournisseur(
      CommandeFournisseurDto commandeCreated, CommandeFournisseurDto commande) {
    assert commandeCreated.getCode().equals(commande.getCode());
    assert commandeCreated.getEtatCommande().equals(commande.getEtatCommande());
    assertNotNull(commandeCreated.getIdEntreprise());
    if (commande.getFournisseur() != null) {
      assertNotNull(commandeCreated.getFournisseur());
      assert commandeCreated.getFournisseur().getNom().equals(commande.getFournisseur().getNom());
    }
    if (commande.getLigneCommandeFournisseurs() != null
        && !commande.getLigneCommandeFournisseurs().isEmpty()) {
      assertNotNull(commandeCreated.getLigneCommandeFournisseurs());
      assert !commandeCreated.getLigneCommandeFournisseurs().isEmpty();
    }
  }

  protected VenteDto buildVente(Map<String, String> row) {
    var fake = new Faker();
    return VenteDto.builder()
        .code(resolveValueWithEmpty(row, "code", fake.bothify("VTE###")))
        .dateVente(Instant.now())
        .commentaire(resolveValueWithEmpty(row, "commentaire", fake.lorem().sentence()))
        .ligneVentes(buildLignesVente(row))
        .build();
  }

  private List<LigneVenteDto> buildLignesVente(Map<String, String> row) {
    List<LigneVenteDto> lignes = new ArrayList<>();

    // Créer une ligne de vente avec un article
    LigneVenteDto ligne =
        LigneVenteDto.builder()
            .article(buildArticleForVente(row))
            .quantite(resolveBigDecimalValueWithEmpty(row, "quantite", 1.0))
            .prixUnitaire(resolveBigDecimalValueWithEmpty(row, "prixUnitaire", 100.0))
            .build();

    lignes.add(ligne);
    return lignes;
  }

  private ArticleDto buildArticleForVente(Map<String, String> row) {
    var fake = new Faker();
    return ArticleDto.builder()
        .codeArticle(resolveValueWithEmpty(row, "articleCode", fake.bothify("ART###")))
        .designation(
            resolveValueWithEmpty(row, "articleDesignation", fake.commerce().productName()))
        .prixUnitaireHt(resolveBigDecimalValueWithEmpty(row, "articlePrixHt", 100.0))
        .prixUnitaireTtc(resolveBigDecimalValueWithEmpty(row, "articlePrixTtc", 119.25))
        .build();
  }

  protected void assertVente(VenteDto venteCreated, VenteDto vente) {
    assert venteCreated.getCode().equals(vente.getCode());
    assertNotNull(venteCreated.getIdEntreprise());
    if (vente.getCommentaire() != null) {
      assert venteCreated.getCommentaire().equals(vente.getCommentaire());
    }
    if (vente.getLigneVentes() != null && !vente.getLigneVentes().isEmpty()) {
      assertNotNull(venteCreated.getLigneVentes());
      assert !venteCreated.getLigneVentes().isEmpty();
      // Vérifier la première ligne de vente
      LigneVenteDto ligneCreated = venteCreated.getLigneVentes().get(0);
      LigneVenteDto ligneExpected = vente.getLigneVentes().get(0);
      if (ligneExpected.getArticle() != null) {
        assertNotNull(ligneCreated.getArticle());
        assert ligneCreated
            .getArticle()
            .getCodeArticle()
            .equals(ligneExpected.getArticle().getCodeArticle());
      }
      assert ligneCreated.getQuantite().compareTo(ligneExpected.getQuantite()) == 0;
      assert ligneCreated.getPrixUnitaire().compareTo(ligneExpected.getPrixUnitaire()) == 0;
    }
  }

  protected MvtStkDto buildMouvementStock(Map<String, String> row) {
    var fake = new Faker();
    return MvtStkDto.builder()
        .dateMvt(Instant.now())
        .quantite(resolveBigDecimalValueWithEmpty(row, "quantite", 10.0))
        .article(buildArticleForMouvement(row))
        .typeMvtStk(resolveTypeMvtStk(row, "typeMvtStk", TypeMvtStk.ENTREE))
        .sourceMvtStk(resolveSourceMvtStk(row, "sourceMvtStk", SourceMvtStk.COMMANDE_FOURNISSEUR))
        .build();
  }

  private ArticleDto buildArticleForMouvement(Map<String, String> row) {
    var fake = new Faker();
    return ArticleDto.builder()
        .id(resolveLongValue(row, "articleId", fake.number().randomNumber()))
        .codeArticle(resolveValueWithEmpty(row, "articleCode", fake.bothify("ART###")))
        .designation(
            resolveValueWithEmpty(row, "articleDesignation", fake.commerce().productName()))
        .prixUnitaireHt(resolveBigDecimalValueWithEmpty(row, "articlePrixHt", 100.0))
        .prixUnitaireTtc(resolveBigDecimalValueWithEmpty(row, "articlePrixTtc", 119.25))
        .build();
  }

  private TypeMvtStk resolveTypeMvtStk(
      Map<String, String> row, String key, TypeMvtStk defaultValue) {
    String value = resolveValueWithEmpty(row, key, null);
    if (value == null || value.isEmpty()) {
      return defaultValue;
    }
    try {
      return TypeMvtStk.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return defaultValue;
    }
  }

  private SourceMvtStk resolveSourceMvtStk(
      Map<String, String> row, String key, SourceMvtStk defaultValue) {
    String value = resolveValueWithEmpty(row, key, null);
    if (value == null || value.isEmpty()) {
      return defaultValue;
    }
    try {
      return SourceMvtStk.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return defaultValue;
    }
  }

  private Long resolveLongValue(Map<String, String> row, String key, Number randomValue) {
    String value = resolveValueWithEmpty(row, key, null);
    if (value == null || value.isEmpty()) {
      return randomValue.longValue();
    }
    if (value.equalsIgnoreCase("random")) {
      return randomValue.longValue();
    }
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      return randomValue.longValue();
    }
  }

  protected void assertMouvementStock(MvtStkDto mvtCreated, MvtStkDto mvt) {
    assert mvtCreated.getQuantite().compareTo(mvt.getQuantite()) == 0;
    assert mvtCreated.getTypeMvtStk().equals(mvt.getTypeMvtStk());
    assert mvtCreated.getSourceMvtStk().equals(mvt.getSourceMvtStk());
    if (mvt.getArticle() != null) {
      assertNotNull(mvtCreated.getArticle());
      if (mvt.getArticle().getCodeArticle() != null) {
        assert mvtCreated.getArticle().getCodeArticle().equals(mvt.getArticle().getCodeArticle());
      }
    }
  }
}
