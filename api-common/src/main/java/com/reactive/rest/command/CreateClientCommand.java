/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonRootName("client")
public class CreateClientCommand {

  @NotBlank
  @JsonProperty("name")
  private String name;
}
