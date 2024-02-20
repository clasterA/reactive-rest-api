/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.reactive.rest.enums.TransactionTypeEnum;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateTransactionCommandTest {

  @Test
  @DisplayName("Integrity test, create transaction command")
  void createTransactionCommandIntegrityTest() {

    var accGuid = UUID.randomUUID();
    var corrAccGuid = UUID.randomUUID();

    var command =
        CreateTransactionCommand.builder()
            .accGuid(accGuid)
            .corrAccGuid(corrAccGuid)
            .trxAmount(new BigDecimal(BigInteger.TEN))
            .trxCurrency("EUR")
            .trxType(TransactionTypeEnum.DEBIT)
            .build();

    assertThat(command).isNotNull();
    assertThat(command.getAccGuid()).isEqualTo(accGuid);
    assertThat(command.getCorrAccGuid()).isEqualTo(corrAccGuid);
    assertThat(command.getTrxAmount()).isEqualTo(new BigDecimal(BigInteger.TEN));
    assertThat(command.getTrxCurrency()).isEqualTo("EUR");
    assertThat(command.getTrxType()).isEqualTo(TransactionTypeEnum.DEBIT);
  }
}
