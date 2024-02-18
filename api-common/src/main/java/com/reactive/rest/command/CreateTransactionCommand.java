/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.reactive.rest.enums.TransactionTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonRootName("transaction")
public class CreateTransactionCommand {

  @NotNull
  @JsonProperty("accGuid")
  private UUID accGuid;

  @NotNull
  @JsonProperty("trxAmount")
  private BigDecimal trxAmount;

  @NotBlank
  @JsonProperty("trxCurrency")
  private String trxCurrency;

  @NotNull
  @JsonProperty("trxType")
  private TransactionTypeEnum trxType;
}
