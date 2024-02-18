/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateTransactionCommand;
import com.reactive.rest.dto.Transaction;
import com.reactive.rest.error.CommonListOfError;
import com.reactive.rest.mapper.CommonMapper;
import com.reactive.rest.repository.TransactionEntity;
import com.reactive.rest.repository.TransactionRepository;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final AccountService accountService;
  private final TransactionRepository transactionRepository;
  private final CommonListOfError commonListOfError;
  private final CommonMapper mapper;

  @Override
  public @NotNull Mono<Transaction> createTransaction(CreateTransactionCommand command) {

    return getLastTransactionForAccount(command.getAccGuid())
        .map(transaction -> createTransactionRequest(command, transaction))
        .flatMap(transactionRepository::save)
        .onErrorResume(
            ex -> commonListOfError.badRequestError("Create transaction", ex.getMessage()))
        .map(mapper::map);
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

  /**
   * A debit entry in an account represents a transfer of value to that account, and a credit entry
   * represents a transfer from the account.
   *
   * @param command
   * @param transaction
   * @return
   */
  private TransactionEntity createTransactionRequest(
      CreateTransactionCommand command, Transaction transaction) {

    var newTransaction = new Transaction();
    newTransaction.setGuid(UUID.randomUUID());
    newTransaction.setAccGuid(transaction.getAccGuid());
    newTransaction.setAccCurrency(transaction.getAccCurrency());
    newTransaction.setTrxType(command.getTrxType());
    newTransaction.setTrxAmount(command.getTrxAmount());
    newTransaction.setTrxCurrency(command.getTrxCurrency());
    newTransaction.setBeginAmount(transaction.getEndAmount());

    switch (command.getTrxType()) {
      case DEBIT ->
          newTransaction.setEndAmount(
              newTransaction.getBeginAmount().add(newTransaction.getTrxAmount()));
      case CREDIT ->
          newTransaction.setEndAmount(
              newTransaction.getBeginAmount().subtract(newTransaction.getTrxAmount()));
    }

    return mapper.map(newTransaction);
  }
}
