Feature: Modification de client
  En tant que entreprise
  Je veux modifier un client

  Scenario Outline: 1.0 modification d'un client
    Given les informations du client à "modifier" avec "<referenceIdContexte>" sont
      | nom   | prenom   | mail   | numTel   | photo   | adresse1   | adresse2   | ville   | pays   | codePostale   |
      | <nom> | <prenom> | <mail> | <numTel> | <photo> | <adresse1> | <adresse2> | <ville> | <pays> | <codePostale> |
    And "creation" du client avec "<referenceIdContexte>"
      | rien |
      |      |
    And "modification" du client avec "<referenceIdContexte>"
      | nom         | prenom         | mail         | numTel         | photo         | adresse1         | adresse2         | ville         | pays         | codePostale         |
      | <nomModife> | <prenomModife> | <mailModife> | <numTelModife> | <photoModife> | <adresse1Modife> | <adresse2Modife> | <villeModife> | <paysModife> | <codePostaleModife> |
    When "modifier" le client avec "<referenceIdContexte>"
    Then le client est "modifié" avec "<referenceIdContexte>"
      | nom         | prenom         | mail         | numTel         | photo         | adresse1         | adresse2         | ville         | pays         | codePostale         |
      | <nomModife> | <prenomModife> | <mailModife> | <numTelModife> | <photoModife> | <adresse1Modife> | <adresse2Modife> | <villeModife> | <paysModife> | <codePostaleModife> |

    Examples:
      | referenceIdContexte        | nom    | prenom | mail                | numTel     | photo | adresse1 | adresse2 | ville | pays   | codePostale | nomModife | prenomModife | mailModife              | numTelModife | photoModife | adresse1Modife | adresse2Modife | villeModife | paysModife | codePostaleModife |
      | update-client-scenario-1.0 | random | random | random              | random     | random| random   | random   | random| random | random      | random    | random       | random                  | random       | random      | random         | random         | random      | random     | random            |
