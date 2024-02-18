/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.dto.ExchangeRate;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {

  @NotNull
  Mono<ExchangeRate> getRateBaseCurrencyAndCurrency(
      @NotBlank String baseCurrency, @NotBlank String currency);
}
