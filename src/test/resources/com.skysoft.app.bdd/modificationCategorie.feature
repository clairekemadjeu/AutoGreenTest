Feature: Modification de la categorie
  En tant que entreprise
  Je veux modifier une categorie

  Scenario Outline: 1.0 modification d'une categorie
    Given les informations de la categorie à "modifier" avec "<referenceIdContexte>" sont
      | code   | designation   |
      | <code> | <designation> |
    And "creation" de la categorie avec "<referenceIdContexte>"
      | rien |
      |      |
    And "modification" de la categorie avec "<referenceIdContexte>"
      | code         | designation         |
      | <codeModife> | <designationModife> |
    When "modifier" la categorie avec "<referenceIdContexte>"
    Then la categorie est "modifiée" avec "<referenceIdContexte>"
      | code         | designation         |
      | <codeModife> | <designationModife> |

    Examples:
      | referenceIdContexte           | code   | designation | codeModife | designationModife |
      | update-categorie-scenario-1.0 | random | random      | random     | random            |
