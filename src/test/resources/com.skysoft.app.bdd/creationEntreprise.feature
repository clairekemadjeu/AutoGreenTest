Feature: Creation d'entreprise
  En tant que utilisateur
  Je veux créer une entreprise

  Scenario Outline: 1.0 création d'une entreprise
    Given les informations de l'entreprise à "créer" avec "<referenceIdContexte>" sont
      | nom   | description   | email   | password   | codeFiscal   | adresse1 | adresse2 | ville  | pays     | codePostale | numTel   | siteWeb   |
      | <nom> | <description> | <email> | <password> | <codeFiscal> | douala   | Rue 12   | douala | cameroun | 2536        | <numTel> | <siteWeb> |
    When "créer" l'entreprise avec "<referenceIdContexte>"
    Then l'entreprise est "créée" avec "<referenceIdContexte>"
      | nom   | description   | email   | codeFiscal   | adresse1 | adresse2 | ville  | pays     | codePostale | numTel   | siteWeb   |
      | <nom> | <description> | <email> | <codeFiscal> | douala   | Rue 12   | douala | cameroun | 2536        | <numTel> | <siteWeb> |

    Examples:
      | referenceIdContexte          | nom        | description                    | email         | password | codeFiscal | numTel    | siteWeb      |
      | create-business-scenario-1.0 | Easy Stock | Entreprise de gestion de stock | test@test.com | test1234 | 354374637  | 653677377 | www.test.com |
