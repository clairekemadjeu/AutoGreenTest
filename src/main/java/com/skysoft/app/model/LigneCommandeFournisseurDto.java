package com.skysoft.app.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeFournisseurDto {

  private Long id;
  private ArticleDto article;
  private CommandeFournisseurDto commandeFournisseur;
  private BigDecimal quantite;
  private BigDecimal prixUnitaire;
  private Long idEntreprise;
}
