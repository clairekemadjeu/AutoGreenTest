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
public class LigneVenteDto {

  private Long id;
  private VenteDto vente;
  private ArticleDto article;
  private BigDecimal quantite;
  private BigDecimal prixUnitaire;
  private Long idEntreprise;
}
