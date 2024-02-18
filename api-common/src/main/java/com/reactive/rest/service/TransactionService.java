/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateTransactionCommand;
import com.reactive.rest.dto.Transaction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
public interface TransactionService {

  @NotNull
  Mono<List<Transaction>> createTransactions(@Valid CreateTransactionCommand command);

  @NotNull
  Mono<Transaction> getLastTransactionForAccount(@NotNull UUID accGuid);

  @NotNull
  Mono<List<Transaction>> getTransactionListForAccount(@NotNull UUID accGuid, int page);
}
