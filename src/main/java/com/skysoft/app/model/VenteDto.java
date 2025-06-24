package com.skysoft.app.model;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenteDto {
  private Long id;
  private String code;
  private Instant dateVente;
  private String commentaire;
  private List<LigneVenteDto> ligneVentes;
  private Long idEntreprise;
}
