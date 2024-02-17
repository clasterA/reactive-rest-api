/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateClientCommandTest {

  @Test
  @DisplayName("Create client command test.")
  void clientStatusEnumValueTest() {

    var command = CreateClientCommand.builder().name("Test client").build();

    assertThat(command).isNotNull();
    assertThat(command.getName()).isEqualTo("Test client");
  }
}
