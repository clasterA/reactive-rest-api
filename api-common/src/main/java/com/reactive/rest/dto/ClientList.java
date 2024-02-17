/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import lombok.Data;

@Data
@JsonRootName("client")
public class ClientList {

  @JsonProperty("clients")
  private List<Client> clients;
}
