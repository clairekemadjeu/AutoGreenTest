Feature: Modification de fournisseur
  En tant que entreprise
  Je veux modifier un fournisseur

  Scenario Outline: 1.0 modification d'un fournisseur
    Given les informations du fournisseur à "modifier" avec "<referenceIdContexte>" sont
      | nom   | prenom   | mail   | numTel   | photo   | adresse1   | adresse2   | ville   | pays   | codePostale   |
      | <nom> | <prenom> | <mail> | <numTel> | <photo> | <adresse1> | <adresse2> | <ville> | <pays> | <codePostale> |
    And "creation" du fournisseur avec "<referenceIdContexte>"
      | rien |
      |      |
    And "modification" du fournisseur avec "<referenceIdContexte>"
      | nom         | prenom         | mail         | numTel         | photo         | adresse1         | adresse2         | ville         | pays         | codePostale         |
      | <nomModife> | <prenomModife> | <mailModife> | <numTelModife> | <photoModife> | <adresse1Modife> | <adresse2Modife> | <villeModife> | <paysModife> | <codePostaleModife> |
    When "modifier" le fournisseur avec "<referenceIdContexte>"
    Then le fournisseur est "modifié" avec "<referenceIdContexte>"
      | nom         | prenom         | mail         | numTel         | photo         | adresse1         | adresse2         | ville         | pays         | codePostale         |
      | <nomModife> | <prenomModife> | <mailModife> | <numTelModife> | <photoModife> | <adresse1Modife> | <adresse2Modife> | <villeModife> | <paysModife> | <codePostaleModife> |

    Examples:
      | referenceIdContexte             | nom      | prenom  | mail                 | numTel     | photo | adresse1 | adresse2 | ville | pays   | codePostale | nomModife | prenomModife | mailModife               | numTelModife | photoModife | adresse1Modife | adresse2Modife | villeModife | paysModife | codePostaleModife |
      | update-fournisseur-scenario-1.0 | random   | random  | random               | random     | random| random   | random   | random| random | random      | random    | random       | random                   | random       | random      | random         | random         | random      | random     | random            |
