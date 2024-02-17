/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.config.BaseIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceTest extends BaseIntegrationTest {

  @Autowired AccountService accountService;

  @Test
  @DisplayName("Test should return client accounts by client guid from postgresql db.")
  void shouldGetClientAccountListByClientGuidTest() {

    var accounts = accountService.getClientAccountList(clientGuid).block();

    Assertions.assertThat(accounts).isNotNull();
    Assertions.assertThat(accounts).hasSize(1);
    Assertions.assertThat(accounts.getFirst().getGuid()).isEqualTo(accountGuid);
    Assertions.assertThat(accounts.getFirst().getClientGuid()).isEqualTo(clientGuid);
    Assertions.assertThat(accounts.getFirst().getClientName()).isEqualTo("TestClient");
    Assertions.assertThat(accounts.getFirst().getName()).isEqualTo("Euro account");
    Assertions.assertThat(accounts.getFirst().getCurrency()).isEqualTo("EUR");
  }
}
