/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reactive.rest.enums.ClientStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class Client {

  @JsonProperty("guid")
  private UUID guid;

  @JsonProperty("name")
  private String name;

  @JsonProperty("status")
  private ClientStatusEnum status;

  @JsonProperty("createdAt")
  private LocalDateTime createdAt;
}
