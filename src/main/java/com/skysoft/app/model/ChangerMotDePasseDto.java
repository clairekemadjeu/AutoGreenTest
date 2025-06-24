package com.skysoft.app.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangerMotDePasseDto {

  private Long id;
  private String motDePasse;
  private String confirmMotDePasse;
}
