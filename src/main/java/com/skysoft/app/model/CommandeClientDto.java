package com.skysoft.app.model;

import com.skysoft.app.model.enumeration.EtatCommande;
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
public class CommandeClientDto {

  private Long id;
  private String code;
  private Instant dateCommande;
  private EtatCommande etatCommande;
  private ClientDto client;
  private Long idEntreprise;
  private List<LigneCommandeClientDto> ligneCommandeClients;

  public boolean isCommandeLivree() {
    return EtatCommande.LIVREE.equals(this.etatCommande);
  }
}
