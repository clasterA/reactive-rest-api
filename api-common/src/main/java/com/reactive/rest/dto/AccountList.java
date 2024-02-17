/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import lombok.Data;

@Data
@JsonRootName("account")
public class AccountList {

  @JsonProperty("accounts")
  private List<Account> accounts;
}
