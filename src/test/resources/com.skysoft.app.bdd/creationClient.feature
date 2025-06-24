Feature: Creation de client
  En tant que entreprise
  Je veux créer un client

  Scenario Outline: 1.0 création d'un client
    Given les informations du client à "créer" avec "<referenceIdContexte>" sont
      | nom   | prenom   | mail   | numTel   | photo   | adresse1   | adresse2   | ville   | pays   | codePostale   |
      | <nom> | <prenom> | <mail> | <numTel> | <photo> | <adresse1> | <adresse2> | <ville> | <pays> | <codePostale> |
    When "créer" le client avec "<referenceIdContexte>"
    Then le client est "créé" avec "<referenceIdContexte>"
      | nom   | prenom   | mail   | numTel   | photo   | adresse1   | adresse2   | ville   | pays   | codePostale   |
      | <nom> | <prenom> | <mail> | <numTel> | <photo> | <adresse1> | <adresse2> | <ville> | <pays> | <codePostale> |

    Examples:
      | referenceIdContexte        | nom      | prenom  | mail                | numTel      | photo                | adresse1        | adresse2 | ville     | pays     | codePostale |
      | create-client-scenario-1.0 | random   | random  | random              | random      | random               | random          | random   | random    | random   | random      |
      | create-client-scenario-1.1 | Dupont   | Jean    | jean.dupont@test.fr | 0123456789  | http://photo.com/1   | 123 Rue de Test | Apt 4    | Paris     | France   | 75001       |
      | create-client-scenario-1.2 | Martin   | Marie   | marie.martin@test.fr| 0987654321  | http://photo.com/2   | 456 Avenue Test | Bât B    | Lyon      | France   | 69000       |
