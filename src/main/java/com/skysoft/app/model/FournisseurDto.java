package com.skysoft.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurDto {

  private Long id;
  private String nom;
  private String prenom;
  private AdresseDto adresse;
  private String photo;
  private String mail;
  private String numTel;
  private Long idEntreprise;

  @JsonIgnore private List<CommandeFournisseurDto> commandeFournisseurs;
}
