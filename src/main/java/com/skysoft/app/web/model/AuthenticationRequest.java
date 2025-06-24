package com.skysoft.app.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

  @NotBlank(message = "login not blank")
  @NotEmpty
  private String login;

  @NotEmpty(message = "Password not empty")
  private String password;
}
