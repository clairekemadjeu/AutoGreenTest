Feature: Validation d'article
  En tant que entreprise
  Je veux valider les données d'un article avant sa création

  Scenario Outline: 1.0 validation des champs obligatoires d'un article
    Given les informations de l'article à "valider" avec "<referenceIdContexte>" sont
      | codeArticle   | designation   | prixUnitaireHt   | prixUnitaireTtc   | tauxTva   | photo   | categorieCode   | categorieDesignation   |
      | <codeArticle> | <designation> | <prixUnitaireHt> | <prixUnitaireTtc> | <tauxTva> | <photo> | <categorieCode> | <categorieDesignation> |
    When "valider" l'article avec "<referenceIdContexte>"
    Then l'article a des erreurs de validation avec "<referenceIdContexte>"
      | messageErreur   |
      | <messageErreur> |

    Examples:
      | referenceIdContexte           | codeArticle | designation | prixUnitaireHt | prixUnitaireTtc | tauxTva | photo | categorieCode | categorieDesignation | messageErreur                                   |
      | validate-article-scenario-1.0 |             | Test        | 100.00         | 119.25          | 19.25   | url   | CAT001        | Test                 | Veuillez renseigner le code de l'article        |
      | validate-article-scenario-1.1 | ART001      |             | 100.00         | 119.25          | 19.25   | url   | CAT001        | Test                 | Veuillez renseigner la designation de l'article |
