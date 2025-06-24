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
public class ArticleDto {

  private Long id;
  private String codeArticle;
  private String designation;
  private BigDecimal prixUnitaireHt;
  private BigDecimal prixUnitaireTtc;
  private BigDecimal tauxTva;
  private String photo;
  private Long idEntreprise;
  private CategorieDto categorie;
}
