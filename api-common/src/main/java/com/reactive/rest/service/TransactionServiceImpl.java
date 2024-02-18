/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateTransactionCommand;
import com.reactive.rest.dto.Transaction;
import com.reactive.rest.enums.TransactionTypeEnum;
import com.reactive.rest.error.CommonListOfError;
import com.reactive.rest.mapper.CommonMapper;
import com.reactive.rest.repository.TransactionEntity;
import com.reactive.rest.repository.TransactionRepository;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final AccountService accountService;
  private final TransactionRepository transactionRepository;
  private final CommonListOfError commonListOfError;
  private final CommonMapper mapper;

  @Value("${trx.page.size:5}")
  protected final int pageSize = 5;

  @Override
  @Transactional
  public @NotNull Mono<List<Transaction>> createTransactions(CreateTransactionCommand command) {

    return getLastTransactionForAccount(command.getAccGuid())
        .flatMap(transaction -> createTransactionRequest(command, transaction, true))
        .flatMap(
            trx1 -> {
              if (command.getCorrAccGuid() != null) {
                return getLastTransactionForAccount(command.getCorrAccGuid())
                    .flatMap(transaction -> createTransactionRequest(command, transaction, false))
                    .flatMap(trx2 -> Mono.just(List.of(trx1, trx2)));
              } else {
                return Mono.just(List.of(trx1));
              }
            })
        .flatMap(trxList -> transactionRepository.saveAll(trxList).map(mapper::map).collectList())
        .onErrorResume(
            ex -> commonListOfError.badRequestError("Create transactions", ex.getMessage()));
  }

  @Override
  public @NotNull Mono<Transaction> getLastTransactionForAccount(@NotNull UUID accGuid) {

    return transactionRepository
        .getLastTransactionForAccount(accGuid)
        .map(mapper::map)
        .switchIfEmpty(
            accountService
                .getAccountByGuid(accGuid)
                .flatMap(
                    account -> {
                      var trx = new Transaction();
                      trx.setAccGuid(account.getGuid());
                      trx.setAccCurrency(account.getCurrency());
                      trx.setTrxAmount(BigDecimal.ZERO);
                      trx.setBeginAmount(BigDecimal.ZERO);
                      trx.setEndAmount(BigDecimal.ZERO);
                      return Mono.just(trx);
                    }));
  }

  @Override
  public @NotNull Mono<List<Transaction>> getTransactionListForAccount(
      @NotNull UUID accGuid, int page) {

    // -1 is added to start get first page with number 1
    var pageOffset = (page - 1) * pageSize;

    return Mono.defer(
            () ->
                transactionRepository
                    .getLastTransactionForAccount(accGuid, pageOffset, pageSize)
                    .collectList()
                    .map(mapper::mapTransactionList))
        .doOnEach(clientList -> log.debug("Transaction data : {}", clientList))
        .onErrorResume(
            ex ->
                commonListOfError.badRequestError(
                    "Get transaction list for account", ex.getMessage()));
  }

  /**
   * A debit entry in an account represents a transfer of value to that account, and a credit entry
   * represents a transfer from the account.
   *
   * @param command - create transaction command
   * @param transaction - last transaction for selected account
   * @param mainAccount - when true, create transaction for main account, when false create
   *     transaction for correspondence account
   * @return TransactionEntity
   */
  private Mono<TransactionEntity> createTransactionRequest(
      CreateTransactionCommand command, Transaction transaction, boolean mainAccount) {

    var reverseTrxType =
        command.getTrxType().equals(TransactionTypeEnum.CREDIT)
            ? TransactionTypeEnum.DEBIT
            : TransactionTypeEnum.CREDIT;
    var trxType = mainAccount ? command.getTrxType() : reverseTrxType;

    var newTransaction = new Transaction();
    newTransaction.setGuid(UUID.randomUUID());
    newTransaction.setAccGuid(transaction.getAccGuid());
    newTransaction.setAccCurrency(transaction.getAccCurrency());
    newTransaction.setTrxType(trxType);
    newTransaction.setTrxAmount(command.getTrxAmount());
    newTransaction.setTrxCurrency(command.getTrxCurrency());
    newTransaction.setBeginAmount(transaction.getEndAmount());

    switch (trxType) {
      case DEBIT ->
          newTransaction.setEndAmount(
              newTransaction.getBeginAmount().add(newTransaction.getTrxAmount()));
      case CREDIT ->
          newTransaction.setEndAmount(
              newTransaction.getBeginAmount().subtract(newTransaction.getTrxAmount()));
    }

    return Mono.just(mapper.map(newTransaction));
  }
}
