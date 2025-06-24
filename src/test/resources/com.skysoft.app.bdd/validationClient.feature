Feature: Validation de client
  En tant que entreprise
  Je veux valider les données d'un client avant sa création

  Scenario Outline: 1.0 validation des champs obligatoires d'un client
    Given les informations du client à "valider" avec "<referenceIdContexte>" sont
      | nom   | prenom   | mail   | numTel   | photo   | adresse1   | adresse2   | ville   | pays   | codePostale   |
      | <nom> | <prenom> | <mail> | <numTel> | <photo> | <adresse1> | <adresse2> | <ville> | <pays> | <codePostale> |
    When "valider" le client avec "<referenceIdContexte>"
    Then le client a des erreurs de validation avec "<referenceIdContexte>"
      | messageErreur   |
      | <messageErreur> |

    Examples:
      | referenceIdContexte          | nom    | prenom | mail                | numTel     | photo | adresse1 | adresse2 | ville | pays   | codePostale | messageErreur                                        |
      | validate-client-scenario-1.0 |        | Jean   | jean.dupont@test.fr | 0123456789 | url   | 123 Test | Apt 4    | Paris | France | 75001       | Veuillez renseigner le nom du client                 |
      | validate-client-scenario-1.1 | Dupont |        | jean.dupont@test.fr | 0123456789 | url   | 123 Test | Apt 4    | Paris | France | 75001       | Veuillez renseigner le prenom du client              |
      | validate-client-scenario-1.2 | Dupont | Jean   |                     | 0123456789 | url   | 123 Test | Apt 4    | Paris | France | 75001       | Veuillez renseigner le Mail du client                |
      | validate-client-scenario-1.3 | Dupont | Jean   | jean.dupont@test.fr |            | url   | 123 Test | Apt 4    | Paris | France | 75001       | Veuillez renseigner le numero de telephone du client |
