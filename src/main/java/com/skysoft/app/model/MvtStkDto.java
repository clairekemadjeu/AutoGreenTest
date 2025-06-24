package com.skysoft.app.model;

import java.math.BigDecimal;
import java.time.Instant;

import com.skysoft.app.model.enumeration.SourceMvtStk;
import com.skysoft.app.model.enumeration.TypeMvtStk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvtStkDto {

  private Long id;
  private Instant dateMvt;
  private BigDecimal quantite;
  private ArticleDto article;
  private TypeMvtStk typeMvtStk;
  private SourceMvtStk sourceMvtStk;
  private Long idEntreprise;
}
