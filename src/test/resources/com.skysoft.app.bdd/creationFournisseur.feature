Feature: Creation de fournisseur
  En tant que entreprise
  Je veux créer un fournisseur

  Scenario Outline: 1.0 création d'un fournisseur
    Given les informations du fournisseur à "créer" avec "<referenceIdContexte>" sont
      | nom   | prenom   | mail   | numTel   | photo   | adresse1   | adresse2   | ville   | pays   | codePostale   |
      | <nom> | <prenom> | <mail> | <numTel> | <photo> | <adresse1> | <adresse2> | <ville> | <pays> | <codePostale> |
    When "créer" le fournisseur avec "<referenceIdContexte>"
    Then le fournisseur est "créé" avec "<referenceIdContexte>"
      | nom   | prenom   | mail   | numTel   | photo   | adresse1   | adresse2   | ville   | pays   | codePostale   |
      | <nom> | <prenom> | <mail> | <numTel> | <photo> | <adresse1> | <adresse2> | <ville> | <pays> | <codePostale> |

    Examples:
      | referenceIdContexte             | nom        | prenom    | mail                     | numTel      | photo                | adresse1          | adresse2 | ville      | pays     | codePostale |
      | create-fournisseur-scenario-1.0 | random     | random    | random                   | random      | random               | random            | random   | random     | random   | random      |
      | create-fournisseur-scenario-1.1 | TechCorp   | Supplier  | contact@techcorp.com     | 0145678901  | http://photo.com/3   | 789 Business Ave  | Suite 10 | Marseille  | France   | 13000       |
      | create-fournisseur-scenario-1.2 | GlobalSup  | Manager   | manager@globalsup.com    | 0156789012  | http://photo.com/4   | 321 Supply Street | Floor 2  | Toulouse   | France   | 31000       |
