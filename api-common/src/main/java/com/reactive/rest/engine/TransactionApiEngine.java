/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.engine;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;

import com.reactive.rest.command.CreateTransactionCommand;
import com.reactive.rest.dto.TransactionList;
import com.reactive.rest.service.TransactionService;
import com.reactive.rest.utils.CommonUtils;
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
  private final CommonUtils commonUtils;

  protected Mono<TransactionList> createNewTransaction(ServerRequest serverRequest) {

    return serverRequest
        .body(toMono(CreateTransactionCommand.class))
        .flatMap(transactionService::createTransactions)
        .flatMap(
            transactions -> {
              var trxList = new TransactionList();
              trxList.setTransactions(transactions);
              return Mono.just(trxList);
            });
  }

  protected Mono<TransactionList> getTransactionListForAccount(ServerRequest serverRequest) {

    return Mono.just(serverRequest)
        .flatMap(commonUtils::getUrlId)
        .flatMap(
            accGuid ->
                transactionService.getTransactionListForAccount(
                    accGuid, Integer.parseInt(serverRequest.queryParam("page").orElse("0"))))
        .flatMap(
            transactions -> {
              var trxList = new TransactionList();
              trxList.setTransactions(transactions);
              return Mono.just(trxList);
            });
  }
}
