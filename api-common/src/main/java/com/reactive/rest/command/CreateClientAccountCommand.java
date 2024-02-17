/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonRootName("account")
public class CreateClientAccountCommand {

  @NotNull
  @JsonProperty("clientGuid")
  private UUID clientGuid;

  @NotBlank
  @JsonProperty("clientName")
  private String clientName;

  @NotBlank
  @JsonProperty("name")
  private String name;

  @NotBlank
  @JsonProperty("currency")
  private String currency;
}
