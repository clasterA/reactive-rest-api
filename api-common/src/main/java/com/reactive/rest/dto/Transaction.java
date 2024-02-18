/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reactive.rest.enums.TransactionTypeEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class Transaction {

  @JsonProperty("guid")
  private UUID guid;

  @JsonProperty("accGuid")
  private UUID accGuid;

  @JsonProperty("accCurrency")
  private String accCurrency;

  @JsonProperty("beginAmount")
  private BigDecimal beginAmount;

  @JsonProperty("trxAmount")
  private BigDecimal trxAmount;

  @JsonProperty("trxCurrency")
  private String trxCurrency;

  @JsonProperty("endAmount")
  private BigDecimal endAmount;

  @JsonProperty("trxType")
  private TransactionTypeEnum trxType;

  @JsonProperty("createdAt")
  private LocalDateTime createdAt;
}
