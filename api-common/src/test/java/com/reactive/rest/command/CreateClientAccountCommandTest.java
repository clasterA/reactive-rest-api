/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateClientAccountCommandTest {

  @Test
  @DisplayName("Integrity test, create client account command")
  void createClientAccountCommandIntegrityTest() {

    var clientGuid = UUID.randomUUID();

    var command =
        CreateClientAccountCommand.builder()
            .clientGuid(clientGuid)
            .clientName("Test client")
            .name("Test account")
            .currency("EUR")
            .build();

    assertThat(command).isNotNull();
    assertThat(command.getClientGuid()).isEqualTo(clientGuid);
    assertThat(command.getClientName()).isEqualTo("Test client");
    assertThat(command.getName()).isEqualTo("Test account");
    assertThat(command.getCurrency()).isEqualTo("EUR");
  }
}
