Feature: Recherche de vente
  En tant que entreprise
  Je veux rechercher des ventes

  Scenario Outline: 1.0 recherche d'une vente inexistante
    When "rechercher" la vente inexistante avec "<referenceIdContexte>"
    Then aucune vente n'est trouvée avec "<referenceIdContexte>"

    Examples:
      | referenceIdContexte                |
      | search-vente-notfound-scenario-2.0 |
      | search-vente-notfound-scenario-2.1 |
