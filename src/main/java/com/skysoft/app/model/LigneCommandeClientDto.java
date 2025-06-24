package com.skysoft.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeClientDto {

  private Long id;
  private ArticleDto article;
  @JsonIgnore private CommandeClientDto commandeClient;
  private BigDecimal quantite;
  private BigDecimal prixUnitaire;
  private Long idEntreprise;
}
