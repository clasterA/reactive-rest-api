/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api;

import com.reactive.rest.config.BaseIntegrationTest;
import com.reactive.rest.dto.TransactionList;
import com.reactive.rest.enums.TransactionTypeEnum;
import com.reactive.rest.utils.CommonUtils;
import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionApiTest extends BaseIntegrationTest {

  private String createExternalIncomeEurTrxJson;
  private String createExternalOutgoingEurTrxJson;
  private String createInternalMoneyTransferEurDebitTrx;
  private String createInternalMoneyTransferEurCreditTrx;
  private String createInternalMoneyTransferEurToUsdDebitTrx;
  private String createInternalMoneyTransferUsdToGbpCreditTrx;

  @BeforeAll
  @DisplayName("Initialize transactions test variables")
  public void init() {

    createExternalIncomeEurTrxJson =
        CommonUtils.getResourceFileAsString("transactions/createExternalIncomeEurTrx.json");
    Assertions.assertThat(createExternalIncomeEurTrxJson).isNotNull();
    createExternalIncomeEurTrxJson =
        String.format(createExternalIncomeEurTrxJson, accountList.getFirst().getGuid().toString());

    createExternalOutgoingEurTrxJson =
        CommonUtils.getResourceFileAsString("transactions/createExternalOutgoingEurTrx.json");
    Assertions.assertThat(createExternalOutgoingEurTrxJson).isNotNull();
    createExternalOutgoingEurTrxJson =
        String.format(
            createExternalOutgoingEurTrxJson, accountList.getFirst().getGuid().toString());

    createInternalMoneyTransferEurDebitTrx =
        CommonUtils.getResourceFileAsString(
            "transactions/createInternalMoneyTransferEurDebitTrx.json");
    Assertions.assertThat(createInternalMoneyTransferEurDebitTrx).isNotNull();
    createInternalMoneyTransferEurDebitTrx =
        String.format(
            createInternalMoneyTransferEurDebitTrx,
            accountList.getLast().getGuid().toString(),
            accountList.getFirst().getGuid().toString());

    createInternalMoneyTransferEurCreditTrx =
        CommonUtils.getResourceFileAsString(
            "transactions/createInternalMoneyTransferEurCreditTrx.json");
    Assertions.assertThat(createInternalMoneyTransferEurCreditTrx).isNotNull();
    createInternalMoneyTransferEurCreditTrx =
        String.format(
            createInternalMoneyTransferEurCreditTrx,
            accountList.getLast().getGuid().toString(),
            accountList.getFirst().getGuid().toString());

    createInternalMoneyTransferEurToUsdDebitTrx =
        CommonUtils.getResourceFileAsString(
            "transactions/createInternalMoneyTransferEurToUsdDebitTrx.json");
    Assertions.assertThat(createInternalMoneyTransferEurToUsdDebitTrx).isNotNull();
    createInternalMoneyTransferEurToUsdDebitTrx =
        String.format(
            createInternalMoneyTransferEurToUsdDebitTrx,
            accountList.get(1).getGuid().toString(), // USD account
            accountList.getFirst().getGuid().toString()); // EUR account

    createInternalMoneyTransferUsdToGbpCreditTrx =
        CommonUtils.getResourceFileAsString(
            "transactions/createInternalMoneyTransferUsdToGbpCreditTrx.json");
    Assertions.assertThat(createInternalMoneyTransferUsdToGbpCreditTrx).isNotNull();
    createInternalMoneyTransferUsdToGbpCreditTrx =
        String.format(
            createInternalMoneyTransferUsdToGbpCreditTrx,
            accountList.get(1).getGuid().toString(), // USD account
            accountList.get(2).getGuid().toString()); // GBP account
  }

  @Test
  @Order(1)
  @DisplayName(
      "Create external incoming EUR transaction, from route: POST path = /transaction, application/json")
  void shouldCreateExternalIncomingEurTransactionTest() {

    webClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("/transaction").build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(createExternalIncomeEurTrxJson)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(TransactionList.class)
        .value(
            transactionList -> {
              Assertions.assertThat(transactionList.getTransactions().getFirst().getGuid())
                  .isNotNull();
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getBeginAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().getFirst().getEndAmount())
                  .isEqualTo(new BigDecimal("10.01"));
            });
  }

  @Test
  @Order(2)
  @DisplayName(
      "Create external outgoing EUR transaction, from route: POST path = /transaction, application/json")
  void shouldCreateExternalOutgoingEurTransactionTest() {

    webClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("/transaction").build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(createExternalOutgoingEurTrxJson)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(TransactionList.class)
        .value(
            transactionList -> {
              Assertions.assertThat(transactionList.getTransactions().getFirst().getGuid())
                  .isNotNull();
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getBeginAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getEndAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
            });
  }

  @Test
  @Order(3)
  @DisplayName(
      "Create internal money transfer from EUR to EUR - Debit operation, from route: POST path = /transaction, application/json")
  void shouldCreateInternalMoneyTransferEurToEurDebitTransactionTest() {

    // add balance to account
    webClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("/transaction").build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(createExternalIncomeEurTrxJson)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(TransactionList.class)
        .value(
            transactionList -> {
              Assertions.assertThat(transactionList.getTransactions().getFirst().getGuid())
                  .isNotNull();
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getBeginAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().getFirst().getEndAmount())
                  .isEqualTo(new BigDecimal("10.01"));
            });

    // internal money transfer
    webClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("/transaction").build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(createInternalMoneyTransferEurDebitTrx)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(TransactionList.class)
        .value(
            transactionList -> {
              Assertions.assertThat(transactionList.getTransactions()).hasSize(2);
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccGuid())
                  .isEqualTo(accountList.getLast().getGuid());
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getBeginAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().getFirst().getEndAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxType())
                  .isEqualTo(TransactionTypeEnum.DEBIT);
              // second transaction
              Assertions.assertThat(transactionList.getTransactions().getLast().getAccGuid())
                  .isEqualTo(accountList.getFirst().getGuid());
              Assertions.assertThat(transactionList.getTransactions().getLast().getAccCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getLast().getBeginAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getLast().getEndAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxType())
                  .isEqualTo(TransactionTypeEnum.CREDIT);
            });
  }

  @Test
  @Order(4)
  @DisplayName(
      "Create internal money transfer from EUR to EUR - Credit operation, from route: POST path = /transaction, application/json")
  void shouldCreateInternalMoneyTransferEurToEurCreditTransactionTest() {

    // internal money transfer
    webClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("/transaction").build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(createInternalMoneyTransferEurCreditTrx)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(TransactionList.class)
        .value(
            transactionList -> {
              Assertions.assertThat(transactionList.getTransactions()).hasSize(2);
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccGuid())
                  .isEqualTo(accountList.getLast().getGuid());
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getBeginAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getEndAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxType())
                  .isEqualTo(TransactionTypeEnum.CREDIT);
              // second transaction
              Assertions.assertThat(transactionList.getTransactions().getLast().getAccGuid())
                  .isEqualTo(accountList.getFirst().getGuid());
              Assertions.assertThat(transactionList.getTransactions().getLast().getAccCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getLast().getBeginAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().getLast().getEndAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxType())
                  .isEqualTo(TransactionTypeEnum.DEBIT);
            });
  }

  @Test
  @Order(5)
  @DisplayName(
      "Create internal money transfer from EUR to USD - Debit operation, from route: POST path = /transaction, application/json")
  void shouldCreateInternalMoneyTransferEurToUsdDebitTransactionTest() {

    // internal money transfer
    webClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("/transaction").build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(createInternalMoneyTransferEurToUsdDebitTrx)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(TransactionList.class)
        .value(
            transactionList -> {
              Assertions.assertThat(transactionList.getTransactions()).hasSize(2);
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccGuid())
                  .isEqualTo(accountList.get(1).getGuid());
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccCurrency())
                  .isEqualTo("USD");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxCurrency())
                  .isEqualTo("USD");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getBeginAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().getFirst().getEndAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxType())
                  .isEqualTo(TransactionTypeEnum.DEBIT);
              // second transaction
              Assertions.assertThat(transactionList.getTransactions().getLast().getAccGuid())
                  .isEqualTo(accountList.getFirst().getGuid());
              Assertions.assertThat(transactionList.getTransactions().getLast().getAccCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxCurrency())
                  .isEqualTo("USD");
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxAmount())
                  .isEqualTo(new BigDecimal("9.31"));
              Assertions.assertThat(transactionList.getTransactions().getLast().getBeginAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getLast().getEndAmount())
                  .isEqualTo(new BigDecimal("0.70"));
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxType())
                  .isEqualTo(TransactionTypeEnum.CREDIT);
            });
  }

  @Test
  @Order(6)
  @DisplayName(
      "Create internal money transfer from USD to GBP - Credit operation, from route: POST path = /transaction, application/json")
  void shouldCreateInternalMoneyTransferEurToUsdCreditTransactionTest() {

    // internal money transfer
    webClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("/transaction").build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(createInternalMoneyTransferUsdToGbpCreditTrx)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(TransactionList.class)
        .value(
            transactionList -> {
              Assertions.assertThat(transactionList.getTransactions()).hasSize(2);
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccGuid())
                  .isEqualTo(accountList.get(1).getGuid());
              Assertions.assertThat(transactionList.getTransactions().getFirst().getAccCurrency())
                  .isEqualTo("USD");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxCurrency())
                  .isEqualTo("GBP");
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxAmount())
                  .isEqualTo(new BigDecimal("6.30"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getBeginAmount())
                  .isEqualTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getEndAmount())
                  .isEqualTo(new BigDecimal("3.71"));
              Assertions.assertThat(transactionList.getTransactions().getFirst().getTrxType())
                  .isEqualTo(TransactionTypeEnum.CREDIT);
              // second transaction
              Assertions.assertThat(transactionList.getTransactions().getLast().getAccGuid())
                  .isEqualTo(accountList.get(2).getGuid());
              Assertions.assertThat(transactionList.getTransactions().getLast().getAccCurrency())
                  .isEqualTo("GBP");
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxCurrency())
                  .isEqualTo("GBP");
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxAmount())
                  .isEqualByComparingTo(new BigDecimal(5));
              Assertions.assertThat(transactionList.getTransactions().getLast().getBeginAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().getLast().getEndAmount())
                  .isEqualByComparingTo(new BigDecimal(5));
              Assertions.assertThat(transactionList.getTransactions().getLast().getTrxType())
                  .isEqualTo(TransactionTypeEnum.DEBIT);
            });
  }

  @Test
  @Order(7)
  @DisplayName(
      "Get paginated transaction list for account, from route: GET path = /transaction/account, application/json")
  void shouldGetPaginatedTransactionListForAccountTest() {

    webClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/transaction/account/" + accountList.getFirst().getGuid())
                    .queryParam("page", "1")
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(TransactionList.class)
        .value(
            transactionList -> {
              Assertions.assertThat(transactionList.getTransactions()).hasSize(6);

              Assertions.assertThat(transactionList.getTransactions().get(5).getBeginAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().get(5).getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().get(5).getTrxAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(5).getEndAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(5).getTrxType())
                  .isEqualTo(TransactionTypeEnum.DEBIT);

              Assertions.assertThat(transactionList.getTransactions().get(4).getBeginAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(4).getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().get(4).getTrxAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(4).getEndAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().get(4).getTrxType())
                  .isEqualTo(TransactionTypeEnum.CREDIT);

              Assertions.assertThat(transactionList.getTransactions().get(3).getBeginAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().get(3).getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().get(3).getTrxAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(3).getEndAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(3).getTrxType())
                  .isEqualTo(TransactionTypeEnum.DEBIT);

              Assertions.assertThat(transactionList.getTransactions().get(2).getBeginAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(2).getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().get(2).getTrxAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(2).getEndAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().get(2).getTrxType())
                  .isEqualTo(TransactionTypeEnum.CREDIT);

              Assertions.assertThat(transactionList.getTransactions().get(1).getBeginAmount())
                  .isEqualByComparingTo(BigDecimal.ZERO);
              Assertions.assertThat(transactionList.getTransactions().get(1).getTrxCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(transactionList.getTransactions().get(1).getTrxAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(1).getEndAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(1).getTrxType())
                  .isEqualTo(TransactionTypeEnum.DEBIT);

              Assertions.assertThat(transactionList.getTransactions().get(0).getBeginAmount())
                  .isEqualByComparingTo(new BigDecimal("10.01"));
              Assertions.assertThat(transactionList.getTransactions().get(0).getTrxCurrency())
                  .isEqualTo("USD");
              Assertions.assertThat(transactionList.getTransactions().get(0).getTrxAmount())
                  .isEqualByComparingTo(new BigDecimal("9.31"));
              Assertions.assertThat(transactionList.getTransactions().get(0).getEndAmount())
                  .isEqualByComparingTo(new BigDecimal("0.70"));
              Assertions.assertThat(transactionList.getTransactions().get(0).getTrxType())
                  .isEqualTo(TransactionTypeEnum.CREDIT);
            });
  }
}
