/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.dto.ExchangeRate;
import com.reactive.rest.error.CommonListOfError;
import com.reactive.rest.mapper.CommonMapper;
import com.reactive.rest.repository.ExchangeRateRepository;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

  private final ExchangeRateRepository exchangeRateRepository;
  private final CommonListOfError commonListOfError;
  private final CommonMapper mapper;

  @Override
  public @NotNull Mono<ExchangeRate> getRateBaseCurrencyAndCurrency(
      @NotBlank String baseCurrency, @NotBlank String currency) {

    return exchangeRateRepository
        .findFirstByBaseCurrencyAndCurrencyOrderByValidFromDesc(baseCurrency, currency)
        .map(mapper::map)
        .switchIfEmpty(
            Mono.just(new ExchangeRate())
                .flatMap(
                    exchangeRate -> {
                      exchangeRate.setRate(BigDecimal.ZERO);
                      return Mono.just(exchangeRate);
                    }));
  }
}
