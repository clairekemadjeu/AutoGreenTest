Feature: Entrée de stock
  En tant que entreprise
  Je veux enregistrer une entrée de stock

  Scenario Outline: 1.0 création d'une entrée de stock
    Given les informations du mouvement de stock à "créer" avec "<referenceIdContexte>" sont
      | quantite   | articleId   | articleCode   | articleDesignation   | typeMvtStk   | sourceMvtStk   |
      | <quantite> | <articleId> | <articleCode> | <articleDesignation> | <typeMvtStk> | <sourceMvtStk> |
    When "créer" le mouvement de stock avec "<referenceIdContexte>"
    Then le mouvement de stock est "créé" avec "<referenceIdContexte>"
      | quantite   | articleId   | articleCode   | articleDesignation   | typeMvtStk   | sourceMvtStk   |
      | <quantite> | <articleId> | <articleCode> | <articleDesignation> | <typeMvtStk> | <sourceMvtStk> |

    Examples:
      | referenceIdContexte        | quantite | articleId | articleCode | articleDesignation | typeMvtStk | sourceMvtStk         |
      | create-entree-scenario-1.1 | 50       | 1         | ART001      | Ordinateur Dell    | ENTREE     | COMMANDE_FOURNISSEUR |
      | create-entree-scenario-1.2 | 100      | 2         | ART002      | Souris Logitech    | ENTREE     | COMMANDE_FOURNISSEUR |