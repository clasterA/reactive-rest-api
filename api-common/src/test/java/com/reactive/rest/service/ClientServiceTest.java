/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.reactive.rest.command.CreateClientCommand;
import com.reactive.rest.config.BaseIntegrationTest;
import com.reactive.rest.enums.ClientStatusEnum;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientServiceTest extends BaseIntegrationTest {

  @Autowired ClientService clientService;

  @Test
  @DisplayName("Test should create new client.")
  void shouldCreateNewClientTest() {

    var command = CreateClientCommand.builder().name("newClient").build();

    var client = clientService.createClient(command).block();

    Assertions.assertThat(client).isNotNull();
    Assertions.assertThat(client.getGuid()).isNotNull();
    Assertions.assertThat(client.getName()).isEqualTo("newClient");
    Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.ACTIVE);
  }

  @Test
  @DisplayName("Test should client by guid from postgresql db.")
  void shouldGetClientByGuidTest() {

    var client = clientService.getClientByGuid(clientList.getFirst().getGuid()).block();

    Assertions.assertThat(client).isNotNull();
    Assertions.assertThat(client.getGuid()).isNotNull();
    Assertions.assertThat(client.getName()).isEqualTo("TestClient");
    Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.ACTIVE);
  }

  @Test
  @DisplayName("Test should remove client (change it status to closed) by guid from postgresql db.")
  void shouldRemoveClientByGuidTest() {

    var client = clientService.removeClient(clientList.getFirst().getGuid()).block();

    Assertions.assertThat(client).isNotNull();
    Assertions.assertThat(client.getGuid()).isNotNull();
    Assertions.assertThat(client.getName()).isEqualTo("TestClient");
    Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.CLOSED);
  }

  @Test
  @DisplayName("Test should return error, remove client.")
  void shouldReturnErrorRemoveClientTest() {

    Exception exception =
        assertThrows(Exception.class, () -> clientService.removeClient(UUID.randomUUID()).block());

    assertThat(exception).isNotNull();
    assertThat(exception.getMessage())
        .isEqualTo(
            "Bad request. Resource = Remove client, Error = Bad request. Resource = Remove client, Error = Remove client error");
  }
}
