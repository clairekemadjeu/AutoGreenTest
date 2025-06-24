Feature: Creation d'article
  En tant que entreprise
  Je veux créer un article

  Scenario Outline: 1.0 création d'un article
    Given les informations de l'article à "créer" avec "<referenceIdContexte>" sont
      | codeArticle   | designation   | prixUnitaireHt   | prixUnitaireTtc   | tauxTva   | photo   | categorieCode   | categorieDesignation   |
      | <codeArticle> | <designation> | <prixUnitaireHt> | <prixUnitaireTtc> | <tauxTva> | <photo> | <categorieCode> | <categorieDesignation> |
    When "créer" l'article avec "<referenceIdContexte>"
    Then l'article est "créé" avec "<referenceIdContexte>"
      | codeArticle   | designation   | prixUnitaireHt   | prixUnitaireTtc   | tauxTva   | photo   | categorieCode   | categorieDesignation   |
      | <codeArticle> | <designation> | <prixUnitaireHt> | <prixUnitaireTtc> | <tauxTva> | <photo> | <categorieCode> | <categorieDesignation> |

    Examples:
      | referenceIdContexte         | codeArticle | designation     | prixUnitaireHt | prixUnitaireTtc | tauxTva | photo  | categorieCode | categorieDesignation |
      | create-article-scenario-1.0 | random      | random          | random         | random          | random  | random | random        | random               |
      | create-article-scenario-1.1 | ART001      | Ordinateur Dell | 500.00         | 596.25          | 19.25   | url1   | CAT001        | Informatique         |
      | create-article-scenario-1.2 | ART002      | Souris Logitech | 25.00          | 29.81           | 19.25   | url2   | CAT001        | Informatique         |
