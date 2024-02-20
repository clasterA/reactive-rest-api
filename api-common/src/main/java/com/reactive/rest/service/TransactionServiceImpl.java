/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateTransactionCommand;
import com.reactive.rest.dto.ExchangeRate;
import com.reactive.rest.dto.Transaction;
import com.reactive.rest.enums.TransactionTypeEnum;
import com.reactive.rest.error.CommonListOfError;
import com.reactive.rest.mapper.CommonMapper;
import com.reactive.rest.repository.TransactionEntity;
import com.reactive.rest.repository.TransactionRepository;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
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
  private final ExchangeRateService exchangeRateService;
  private final TransactionRepository transactionRepository;
  private final CommonListOfError commonListOfError;
  private final CommonMapper mapper;

  @Value("${trx.page.size:5}")
  protected final int pageSize = 10;

  /**
   * Accept, external incoming transaction - Debit, external outgoing transaction - Credit, internal
   * money transfer - Debit / Credit
   *
   * @param command - Create transaction command
   * @return - created transaction list. One transaction for external incoming / outgoing operation.
   *     Two transaction for internal money transfer.
   */
  @Override
  @Transactional
  public @NotNull Mono<List<Transaction>> createTransactions(CreateTransactionCommand command) {

    return getLastTransactionForAccount(command.getAccGuid())
        .flatMap(
            trx1 -> {
              if (command.getCorrAccGuid() != null) {
                return getLastTransactionForAccount(command.getCorrAccGuid())
                    .flatMap(trx2 -> Mono.just(List.of(trx1, trx2)));
              } else {
                return Mono.just(List.of(trx1));
              }
            })
        .flatMap(
            trxList -> {
              // internal money transfer
              if (trxList.size() == 2) {
                if (!trxList.getFirst().getAccCurrency().equals(command.getTrxCurrency())
                    && !trxList.getLast().getAccCurrency().equals(command.getTrxCurrency())) {
                  return commonListOfError.badRequestError(
                      "Create transactions",
                      "Transaction currency not match any accounts currency");
                }

                switch (command.getTrxType()) {
                  case DEBIT -> {
                    if (!trxList.getFirst().getAccCurrency().equals(command.getTrxCurrency())) {
                      return commonListOfError.badRequestError(
                          "Create transaction not match with receiver account currency",
                          "Transaction currency not match. Money transfer, Debit operation");
                    }
                    return Mono.zip(
                        Mono.just(trxList),
                        exchangeRateService.getRateBaseCurrencyAndCurrency(
                            trxList.getFirst().getAccCurrency(),
                            trxList.getLast().getAccCurrency()));
                  }
                  case CREDIT -> {
                    if (!trxList.getLast().getAccCurrency().equals(command.getTrxCurrency())) {
                      return commonListOfError.badRequestError(
                          "Create transaction not match with receiver account currency",
                          "Transaction currency not match. Money transfer, Credit operation");
                    }
                    return Mono.zip(
                        Mono.just(trxList),
                        exchangeRateService.getRateBaseCurrencyAndCurrency(
                            trxList.getLast().getAccCurrency(),
                            trxList.getFirst().getAccCurrency()));
                  }
                  default -> {
                    return commonListOfError.badRequestError(
                        "Create transaction", "Transaction type not match");
                  }
                }

              } else {
                // external transfer can be only in the selected account currency
                if (!trxList.getFirst().getAccCurrency().equals(command.getTrxCurrency())) {
                  return commonListOfError.badRequestError(
                      "Create transaction", "Transaction currency not match");
                }
                return Mono.zip(
                    Mono.just(trxList),
                    exchangeRateService.getRateBaseCurrencyAndCurrency(
                        trxList.getFirst().getAccCurrency(), command.getTrxCurrency()));
              }
            })
        .flatMap(tuple -> createTransactionRequest(command, tuple.getT1(), tuple.getT2()))
        .flatMap(trxList -> transactionRepository.saveAll(trxList).map(mapper::map).collectList())
        .onErrorResume(
            ex -> commonListOfError.badRequestError("Create transactions error", ex.getMessage()));
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
   * @param transactions - transactions list to processing, can't be more than two transaction
   * @param exchangeRate - pass exchange rate, when transaction currency is different with one of
   *     the account currency transaction for correspondence account
   * @return TransactionEntity
   */
  private Mono<List<TransactionEntity>> createTransactionRequest(
      CreateTransactionCommand command, List<Transaction> transactions, ExchangeRate exchangeRate) {

    List<Transaction> newTransactions = new ArrayList<>();

    transactions.forEach(
        transaction -> {
          var trxType = command.getTrxType();
          var newTransaction = new Transaction();
          newTransaction.setGuid(UUID.randomUUID());
          newTransaction.setAccGuid(transaction.getAccGuid());
          newTransaction.setTrxAmount(command.getTrxAmount());
          newTransaction.setTrxCurrency(command.getTrxCurrency());

          // first transaction for base account
          if (command.getAccGuid().equals(transaction.getAccGuid())) {
            newTransaction.setAccCurrency(transaction.getAccCurrency());
            newTransaction.setBeginAmount(transaction.getEndAmount());
          } else {
            // for second transaction (correspondence account), make reversing transaction type
            trxType =
                command.getTrxType().equals(TransactionTypeEnum.CREDIT)
                    ? TransactionTypeEnum.DEBIT
                    : TransactionTypeEnum.CREDIT;
            newTransaction.setAccCurrency(transaction.getAccCurrency());
            newTransaction.setBeginAmount(transaction.getEndAmount());
          }
          newTransaction.setTrxType(trxType);

          switch (trxType) {
            case DEBIT ->
                newTransaction.setEndAmount(
                    newTransaction.getBeginAmount().add(newTransaction.getTrxAmount()));
            case CREDIT -> {
              var substractAmount = newTransaction.getTrxAmount();
              if (exchangeRate.getCurrency() != null
                  && exchangeRate.getCurrency().equals(transaction.getAccCurrency())) {
                substractAmount =
                    newTransaction
                        .getTrxAmount()
                        .multiply(exchangeRate.getRate())
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
              }
              newTransaction.setTrxAmount(substractAmount);
              newTransaction.setEndAmount(
                  newTransaction.getBeginAmount().subtract(substractAmount));
            }
          }
          newTransactions.add(newTransaction);
        });

    return Mono.just(mapper.mapTransactionEntityList(newTransactions));
  }
}
