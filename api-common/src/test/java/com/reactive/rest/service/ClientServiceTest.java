/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.config.BaseIntegrationTest;
import com.reactive.rest.enums.ClientStatusEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientServiceTest extends BaseIntegrationTest {

  @Autowired ClientService clientService;

  @Test
  @DisplayName("Test should client by guid from postgresql db.")
  void shouldGetClientByGuidTest() {

    var client = clientService.getClientByGuid(userGuid).block();

    Assertions.assertThat(client).isNotNull();
    Assertions.assertThat(client.getGuid()).isNotNull();
    Assertions.assertThat(client.getName()).isEqualTo("TestClient");
    Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.ACTIVE);
  }

  @Test
  @DisplayName("Test should remove client (change it status to closed) by guid from postgresql db.")
  void shouldRemoveClientByGuidTest() {

    var client = clientService.removeClient(userGuid).block();

    Assertions.assertThat(client).isNotNull();
    Assertions.assertThat(client.getGuid()).isNotNull();
    Assertions.assertThat(client.getName()).isEqualTo("TestClient");
    Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.CLOSED);
  }
}
