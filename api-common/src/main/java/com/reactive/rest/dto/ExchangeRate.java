/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ExchangeRate {

  @JsonProperty("baseCurrency")
  private String baseCurrency;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("rate")
  private BigDecimal rate;

  @JsonProperty("validFrom")
  private LocalDate validFrom;
}
