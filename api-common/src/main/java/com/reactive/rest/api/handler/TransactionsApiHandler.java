/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api.handler;

import com.reactive.rest.dto.Transaction;
import com.reactive.rest.engine.TransactionApiEngine;
import com.reactive.rest.service.TransactionService;
import java.net.URI;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TransactionsApiHandler extends TransactionApiEngine {

  public TransactionsApiHandler(TransactionService transactionService) {
    super(transactionService);
  }

  /**
   * Operation create new debit / credit transaction. A debit entry in an account represents a
   * transfer of value to that account, and a credit entry represents a transfer from the account.
   *
   * @return - all the products info as part of ServerResponse
   */
  @SneakyThrows
  public Mono<ServerResponse> createTransaction(ServerRequest serverRequest) {

    return ServerResponse.created(new URI("http://localhost"))
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.createNewTransaction(serverRequest), Transaction.class)
        .onErrorResume(
            e -> {
              log.error("Create new transaction error: " + e.getMessage());
              return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage());
            });
  }
}
