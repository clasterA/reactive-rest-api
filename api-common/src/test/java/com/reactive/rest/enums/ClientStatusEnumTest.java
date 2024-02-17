/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClientStatusEnumTest {

  @Test
  @DisplayName("Client status enum test for value present.")
  void clientStatusEnumValueTest() {

    assertThat(ClientStatusEnum.ACTIVE.getVal()).isEqualTo("Active");
    assertThat(ClientStatusEnum.CLOSED.getVal()).isEqualTo("Closed");
  }

  @Test
  @DisplayName("Client status enum from string test for value present.")
  void clientStatusEnumFromStringTest() {

    assertThat(ClientStatusEnum.fromString("Active")).isEqualTo(ClientStatusEnum.ACTIVE);
    assertThat(ClientStatusEnum.fromString("Closed")).isEqualTo(ClientStatusEnum.CLOSED);
  }
}
