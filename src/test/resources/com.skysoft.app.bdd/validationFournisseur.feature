Feature: Validation de fournisseur
  En tant que entreprise
  Je veux valider les données d'un fournisseur avant sa création

  Scenario Outline: 1.0 validation des champs obligatoires d'un fournisseur
    Given les informations du fournisseur à "valider" avec "<referenceIdContexte>" sont
      | nom   | prenom   | mail   | numTel   | photo   | adresse1   | adresse2   | ville   | pays   | codePostale   |
      | <nom> | <prenom> | <mail> | <numTel> | <photo> | <adresse1> | <adresse2> | <ville> | <pays> | <codePostale> |
    When "valider" le fournisseur avec "<referenceIdContexte>"
    Then le fournisseur a des erreurs de validation avec "<referenceIdContexte>"
      | messageErreur   |
      | <messageErreur> |

    Examples:
      | referenceIdContexte               | nom      | prenom   | mail                 | numTel     | photo | adresse1 | adresse2 | ville | pays   | codePostale | messageErreur                                             |
      | validate-fournisseur-scenario-1.0 |          | Supplier | contact@techcorp.com | 0145678901 | url   | 789 Test | Suite 10 | Paris | France | 75001       | Veuillez renseigner le nom du fournisseur                 |
      | validate-fournisseur-scenario-1.1 | TechCorp |          | contact@techcorp.com | 0145678901 | url   | 789 Test | Suite 10 | Paris | France | 75001       | Veuillez renseigner le prenom du fournisseur              |
      | validate-fournisseur-scenario-1.2 | TechCorp | Supplier |                      | 0145678901 | url   | 789 Test | Suite 10 | Paris | France | 75001       | Veuillez renseigner le Mail du fournisseur                |
      | validate-fournisseur-scenario-1.3 | TechCorp | Supplier | contact@techcorp.com |            | url   | 789 Test | Suite 10 | Paris | France | 75001       | Veuillez renseigner le numero de telephone du fournisseur |
