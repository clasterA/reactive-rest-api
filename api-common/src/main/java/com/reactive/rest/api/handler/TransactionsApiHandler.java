/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api.handler;

import com.reactive.rest.dto.TransactionList;
import com.reactive.rest.engine.TransactionApiEngine;
import com.reactive.rest.service.TransactionService;
import java.net.URI;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
        .body(this.createNewTransaction(serverRequest), TransactionList.class);
  }

  /**
   * Operation return paginated transaction list for selected account
   *
   * @return - all the products info as part of ServerResponse
   */
  public Mono<ServerResponse> getTransactionsForAccount(ServerRequest serverRequest) {

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.getTransactionListForAccount(serverRequest), TransactionList.class);
  }
}
