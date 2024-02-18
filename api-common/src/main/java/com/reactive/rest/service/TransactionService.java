/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateTransactionCommand;
import com.reactive.rest.dto.Transaction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
public interface TransactionService {

  @NotNull
  Mono<Transaction> createTransaction(@Valid CreateTransactionCommand command);

  @NotNull
  Mono<Transaction> getLastTransactionForAccount(@NotNull UUID accGuid);
}
