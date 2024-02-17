/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class Account {

  @JsonProperty("guid")
  private UUID guid;

  @JsonProperty("clientGuid")
  private UUID clientGuid;

  @JsonProperty("clientName")
  private String clientName;

  @JsonProperty("name")
  private String name;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("createdAt")
  private LocalDateTime createdAt;
}
