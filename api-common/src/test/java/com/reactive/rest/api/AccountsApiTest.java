/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api;

import com.reactive.rest.config.BaseIntegrationTest;
import com.reactive.rest.dto.Account;
import com.reactive.rest.dto.AccountList;
import com.reactive.rest.utils.CommonUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountsApiTest extends BaseIntegrationTest {

  private String createNewClientAccountJson;

  @BeforeAll
  @DisplayName("Initialize accounts test variables")
  public void init() {

    createNewClientAccountJson =
        CommonUtils.getResourceFileAsString("accounts/createNewClientAccount.json");
    Assertions.assertThat(createNewClientAccountJson).isNotNull();
    createNewClientAccountJson =
        String.format(createNewClientAccountJson, clientList.getLast().getGuid().toString());
  }

  @Test
  @Order(1)
  @DisplayName("Create client account, from route: POST path = /account, application/json")
  void shouldCreateNewClientExistJsonTest() {

    webClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("/account").build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(createNewClientAccountJson)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Account.class)
        .value(
            account -> {
              Assertions.assertThat(account.getGuid()).isNotNull();
              Assertions.assertThat(account.getClientGuid())
                  .isEqualTo(clientList.getLast().getGuid());
              Assertions.assertThat(account.getClientName())
                  .isEqualTo(clientList.getLast().getName());
              Assertions.assertThat(account.getName()).isEqualTo("USD account");
              Assertions.assertThat(account.getCurrency()).isEqualTo("USD");
            });
  }

  @Test
  @Order(2)
  @DisplayName(
      "Get list of client account, from route: GET path = /account/client/{id}, application/json")
  void shouldGetClientAccountListJsonTest() {

    webClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/account/client/" + clientList.getFirst().getGuid()).build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(AccountList.class)
        .value(
            accountList -> {
              Assertions.assertThat(accountList.getAccounts()).hasSize(3);
              Assertions.assertThat(accountList.getAccounts().getFirst().getCurrency())
                  .isEqualTo("EUR");
              Assertions.assertThat(accountList.getAccounts().get(1).getCurrency())
                  .isEqualTo("USD");
              Assertions.assertThat(accountList.getAccounts().getLast().getCurrency())
                  .isEqualTo("GBP");
            });
  }
}
