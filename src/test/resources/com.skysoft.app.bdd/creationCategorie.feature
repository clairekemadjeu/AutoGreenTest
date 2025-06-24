Feature: Creation de la categorie
  En tant que entreprise
  Je veux créer une categorie

  Scenario Outline: 1.0 création d'une categorie
    Given les informations de la categorie à "créer" avec "<referenceIdContexte>" sont
      | code   | designation   |
      | <code> | <designation> |
    When "créer" la categorie avec "<referenceIdContexte>"
    Then la categorie est "créée" avec "<referenceIdContexte>"
      | code   | designation   |
      | <code> | <designation> |

    Examples:
      | referenceIdContexte           | code   | designation |
      | create-categorie-scenario-1.0 | random | random      |