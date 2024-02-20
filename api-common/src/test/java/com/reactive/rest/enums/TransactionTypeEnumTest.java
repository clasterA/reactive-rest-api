/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionTypeEnumTest {

  @Test
  @DisplayName("Transaction type enum test for value present.")
  void transactionTypeEnumValueTest() {

    assertThat(TransactionTypeEnum.DEBIT.getVal()).isEqualTo("Debit");
    assertThat(TransactionTypeEnum.CREDIT.getVal()).isEqualTo("Credit");
  }

  @Test
  @DisplayName("Transaction type enum from string test for value present.")
  void transactionTypeFromStringTest() {

    assertThat(TransactionTypeEnum.fromString("Debit")).isEqualTo(TransactionTypeEnum.DEBIT);
    assertThat(TransactionTypeEnum.fromString("Credit")).isEqualTo(TransactionTypeEnum.CREDIT);
  }
}
