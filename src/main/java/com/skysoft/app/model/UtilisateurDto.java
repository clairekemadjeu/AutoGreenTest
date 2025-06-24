package com.skysoft.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDto {

  private Long id;
  private String nom;
  private String prenom;
  private String email;
  private Instant dateNaissance;
  private String motDePasse;
  private AdresseDto adresse;
  private String photo;
  private EntrepriseDto entreprise;
  private List<RolesDto> roles;
}
