package com.skysoft.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class EntrepriseDto {

  private Long id;
  private String nom;
  private String description;
  private AdresseDto adresse;
  private String codeFiscal;
  private String photo;
  private String email;
  private String password;
  private String numTel;
  private String siteWeb;

  @JsonIgnore private List<UtilisateurDto> utilisateurs;
}
