/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.reactive.rest.command.CreateTransactionCommand;
import com.reactive.rest.config.BaseIntegrationTest;
import com.reactive.rest.enums.TransactionTypeEnum;
import com.reactive.rest.error.CommonException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTest extends BaseIntegrationTest {

  @Autowired TransactionService transactionService;

  @Test
  @DisplayName(
      "Should return error for (balance >=0 ), when create external outgoing EUR transaction with zero balance")
  void shouldReturnBalanceErrorOutgoingEurTransactionTest() {

    var command =
        CreateTransactionCommand.builder()
            .accGuid(accountList.getFirst().getGuid())
            .trxType(TransactionTypeEnum.CREDIT)
            .trxAmount(BigDecimal.TEN)
            .trxCurrency("EUR")
            .build();

    Exception exception =
        assertThrows(Exception.class, () -> transactionService.createTransactions(command).block());

    assertThat(exception).isInstanceOf(CommonException.class);

    if (exception instanceof CommonException error) {
      assertThat(error.getError().getHttpCode()).isEqualTo(400);
      assertThat(error.getError().getName()).isEqualTo("BAD_REQUEST");
    }
  }

  @Test
  @DisplayName("Should return error, when transaction currency not match for outgoing transaction")
  void shouldReturnErrorTrxCurrencyNotMatchEurTransactionTest() {

    var command =
        CreateTransactionCommand.builder()
            .accGuid(accountList.getFirst().getGuid())
            .trxType(TransactionTypeEnum.CREDIT)
            .trxAmount(BigDecimal.TEN)
            .trxCurrency("LVL")
            .build();

    Exception exception =
        assertThrows(Exception.class, () -> transactionService.createTransactions(command).block());

    assertThat(exception).isInstanceOf(CommonException.class);

    if (exception instanceof CommonException error) {
      assertThat(error.getError().getHttpCode()).isEqualTo(400);
      assertThat(error.getError().getName()).isEqualTo("BAD_REQUEST");
      assertThat(error.getError().getDescription())
          .isEqualTo(
              "Bad request. Resource = Create transactions error, Error = Bad request. Resource = Create transaction, Error = Transaction currency not match");
    }
  }

  @Test
  @DisplayName(
      "Should return error, when transaction currency not match with the receiver account currency - Debit transfer")
  void shouldReturnErrorTrxCurrencyNotMatchInternalDebitTransferEurTransactionTest() {

    var command =
        CreateTransactionCommand.builder()
            .accGuid(accountList.get(1).getGuid()) // USD account
            .corrAccGuid(accountList.getLast().getGuid()) // EUR account
            .trxType(TransactionTypeEnum.DEBIT) // internal transfer from EUR -> USD
            .trxAmount(BigDecimal.TEN)
            .trxCurrency("EUR")
            .build();

    Exception exception =
        assertThrows(Exception.class, () -> transactionService.createTransactions(command).block());

    assertThat(exception).isInstanceOf(CommonException.class);

    if (exception instanceof CommonException error) {
      assertThat(error.getError().getHttpCode()).isEqualTo(400);
      assertThat(error.getError().getName()).isEqualTo("BAD_REQUEST");
      assertThat(error.getError().getDescription())
          .isEqualTo(
              "Bad request. Resource = Create transactions error, Error = Bad request. Resource = Create transaction not match with receiver account currency, Error = Transaction currency not match. Money transfer, Debit operation");
    }
  }

  @Test
  @DisplayName(
      "Should return error, when transaction currency not match with the receiver account currency - Credit transfer")
  void shouldReturnErrorTrxCurrencyNotMatchInternalCreditTransferUsdTransactionTest() {

    var command =
        CreateTransactionCommand.builder()
            .accGuid(accountList.get(1).getGuid()) // USD account
            .corrAccGuid(accountList.getLast().getGuid()) // EUR account
            .trxType(TransactionTypeEnum.CREDIT) // internal transfer from USD -> EUR
            .trxAmount(BigDecimal.TEN)
            .trxCurrency("USD")
            .build();

    Exception exception =
        assertThrows(Exception.class, () -> transactionService.createTransactions(command).block());

    assertThat(exception).isInstanceOf(CommonException.class);

    if (exception instanceof CommonException error) {
      assertThat(error.getError().getHttpCode()).isEqualTo(400);
      assertThat(error.getError().getName()).isEqualTo("BAD_REQUEST");
      assertThat(error.getError().getDescription())
          .isEqualTo(
              "Bad request. Resource = Create transactions error, Error = Bad request. Resource = Create transaction not match with receiver account currency, Error = Transaction currency not match. Money transfer, Credit operation");
    }
  }

  @Test
  @DisplayName(
      "Should return error, when transaction currency not match with any account currency - Debit transfer")
  void shouldReturnErrorTrxCurrencyNotMatchAnyInternalTransferCurrencyTransactionTest() {

    var command =
        CreateTransactionCommand.builder()
            .accGuid(accountList.get(1).getGuid()) // USD account
            .corrAccGuid(accountList.getLast().getGuid()) // EUR account
            .trxType(TransactionTypeEnum.DEBIT) // internal transfer from EUR -> USD
            .trxAmount(BigDecimal.TEN)
            .trxCurrency("LVL")
            .build();

    Exception exception =
        assertThrows(Exception.class, () -> transactionService.createTransactions(command).block());

    assertThat(exception).isInstanceOf(CommonException.class);

    if (exception instanceof CommonException error) {
      assertThat(error.getError().getHttpCode()).isEqualTo(400);
      assertThat(error.getError().getName()).isEqualTo("BAD_REQUEST");
      assertThat(error.getError().getDescription())
          .isEqualTo(
              "Bad request. Resource = Create transactions error, Error = Bad request. Resource = Create transactions, Error = Transaction currency not match any accounts currency");
    }
  }
}
