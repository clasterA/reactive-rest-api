/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRateEntity, Long> {

  Mono<ExchangeRateEntity> findFirstByBaseCurrencyAndCurrencyOrderByValidFromDesc(
      String baseCurrency, String currency);
}
