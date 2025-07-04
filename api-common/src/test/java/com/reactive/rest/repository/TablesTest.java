/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TablesTest {

  @Test
  @DisplayName("Database tables naming test.")
  void databaseTablesNamingTest() {

    Assertions.assertThat(Tables.CLIENT).isEqualTo("rest_api.clients");

    Assertions.assertThat(Tables.ACCOUNT).isEqualTo("rest_api.accounts");

    Assertions.assertThat(Tables.TRANSACTION).isEqualTo("rest_api.transactions");

    Assertions.assertThat(Tables.CURRENCY).isEqualTo("rest_api.currency");

    Assertions.assertThat(Tables.EXCHANGE_RATE).isEqualTo("rest_api.exchange_rate");
  }
}
