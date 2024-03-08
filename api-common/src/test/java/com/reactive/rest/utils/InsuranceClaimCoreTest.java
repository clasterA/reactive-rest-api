/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.reactive.rest.repository.Tables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InsuranceClaimCoreTest {

  @Test
  @DisplayName("Test core tables variable list.")
  void CoreTableVariableTest() {

    var result = new Tables();
    assertThat(result).isNotNull();

    assertThat(Tables.ACCOUNT).isEqualTo("rest_api.accounts");
    assertThat(Tables.CLIENT).isEqualTo("rest_api.clients");
    assertThat(Tables.CURRENCY).isEqualTo("rest_api.currency");
    assertThat(Tables.EXCHANGE_RATE).isEqualTo("rest_api.exchange_rate");
    assertThat(Tables.TRANSACTION).isEqualTo("rest_api.transactions");
  }
}
