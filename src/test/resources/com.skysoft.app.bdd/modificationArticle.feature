Feature: Modification d'article
  En tant que entreprise
  Je veux modifier un article

  Scenario Outline: 1.0 modification d'un article
    Given les informations de l'article à "modifier" avec "<referenceIdContexte>" sont
      | codeArticle   | designation   | prixUnitaireHt   | prixUnitaireTtc   | tauxTva   | photo   | categorieCode   | categorieDesignation   |
      | <codeArticle> | <designation> | <prixUnitaireHt> | <prixUnitaireTtc> | <tauxTva> | <photo> | <categorieCode> | <categorieDesignation> |
    And "creation" de l'article avec "<referenceIdContexte>"
      | rien |
      |      |
    And "modification" de l'article avec "<referenceIdContexte>"
      | codeArticle         | designation         | prixUnitaireHt         | prixUnitaireTtc         | tauxTva         | photo         | categorieCode         | categorieDesignation         |
      | <codeArticleModife> | <designationModife> | <prixUnitaireHtModife> | <prixUnitaireTtcModife> | <tauxTvaModife> | <photoModife> | <categorieCodeModife> | <categorieDesignationModife> |
    When "modifier" l'article avec "<referenceIdContexte>"
    Then l'article est "modifié" avec "<referenceIdContexte>"
      | codeArticle         | designation         | prixUnitaireHt         | prixUnitaireTtc         | tauxTva         | photo         | categorieCode         | categorieDesignation         |
      | <codeArticleModife> | <designationModife> | <prixUnitaireHtModife> | <prixUnitaireTtcModife> | <tauxTvaModife> | <photoModife> | <categorieCodeModife> | <categorieDesignationModife> |

    Examples:
      | referenceIdContexte         | codeArticle | designation     | prixUnitaireHt | prixUnitaireTtc | tauxTva | photo  | categorieCode | categorieDesignation | codeArticleModife | designationModife      | prixUnitaireHtModife | prixUnitaireTtcModife | tauxTvaModife | photoModife | categorieCodeModife | categorieDesignationModife |
      | update-article-scenario-1.0 | random      | random          | random         | random          | random  | random | random        | random               | random            | random                 | random               | random                | random        | random      | random              | random                     |
