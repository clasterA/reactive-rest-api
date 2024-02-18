/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.engine;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;

import com.reactive.rest.command.CreateTransactionCommand;
import com.reactive.rest.dto.Transaction;
import com.reactive.rest.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionApiEngine {

  private final TransactionService transactionService;

  protected Mono<Transaction> createNewTransaction(ServerRequest serverRequest) {

    return serverRequest
        .body(toMono(CreateTransactionCommand.class))
        .flatMap(transactionService::createTransaction);
  }
}
