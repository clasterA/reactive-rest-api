/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.reactive.rest.enums.ClientStatusEnum;
import com.reactive.rest.enums.TransactionTypeEnum;
import com.reactive.rest.repository.AccountEntity;
import com.reactive.rest.repository.ClientEntity;
import com.reactive.rest.repository.ExchangeRateEntity;
import com.reactive.rest.repository.TransactionEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CommonMapperTest {

  private final CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

  @Test
  @DisplayName("Client entity to dto layer mapping test and dto to entity layer mapping test.")
  void clientEntityToDtoAndDtoToEntityTest() {

    var guid = UUID.randomUUID();

    var clientEntity =
        ClientEntity.builder()
            .id(1L)
            .guid(guid)
            .name("Test Name")
            .status(ClientStatusEnum.ACTIVE.getVal())
            .build();

    assertThat(clientEntity).isNotNull();

    var clientDto = mapper.map(clientEntity);

    assertThat(clientDto).isNotNull();
    assertThat(clientDto.getGuid()).isEqualTo(guid);
    assertThat(clientDto.getName()).isEqualTo("Test Name");
    assertThat(clientDto.getStatus()).isEqualTo(ClientStatusEnum.ACTIVE);

    clientDto.setName("Abc");
    clientDto.setStatus(ClientStatusEnum.CLOSED);

    clientEntity = mapper.map(clientDto);
    assertThat(clientEntity).isNotNull();
    assertThat(clientEntity.getGuid()).isEqualTo(guid);
    assertThat(clientEntity.getName()).isEqualTo("Abc");
    assertThat(clientEntity.getStatus()).isEqualTo(ClientStatusEnum.CLOSED.getVal());
  }

  @Test
  @DisplayName("Account entity to dto layer mapping test and dto to entity layer mapping test.")
  void accountEntityToDtoAndDtoToEntityTest() {

    var guid = UUID.randomUUID();
    var clientGuid = UUID.randomUUID();

    var accountEntity =
        AccountEntity.builder()
            .id(1L)
            .guid(guid)
            .clientGuid(clientGuid)
            .clientName("Test client name")
            .name("Test account")
            .currency("EUR")
            .build();

    assertThat(accountEntity).isNotNull();

    var accountDto = mapper.map(accountEntity);

    assertThat(accountDto).isNotNull();
    assertThat(accountDto.getGuid()).isEqualTo(guid);
    assertThat(accountDto.getClientGuid()).isEqualTo(clientGuid);
    assertThat(accountDto.getClientName()).isEqualTo("Test client name");
    assertThat(accountDto.getName()).isEqualTo("Test account");
    assertThat(accountDto.getCurrency()).isEqualTo("EUR");

    accountEntity = mapper.map(accountDto);
    assertThat(accountEntity).isNotNull();
    assertThat(accountEntity.getGuid()).isEqualTo(guid);
    assertThat(accountEntity.getClientGuid()).isEqualTo(clientGuid);
    assertThat(accountEntity.getClientName()).isEqualTo("Test client name");
    assertThat(accountEntity.getName()).isEqualTo("Test account");
    assertThat(accountEntity.getCurrency()).isEqualTo("EUR");
  }

  @Test
  @DisplayName(
      "Exchange rate entity to dto layer mapping test and dto to entity layer mapping test.")
  void exchangeRateEntityToDtoAndDtoToEntityTest() {

    var localDate = LocalDate.now();

    var exchangeRateEntity =
        ExchangeRateEntity.builder()
            .id(1L)
            .baseCurrency("EUR")
            .currency("USD")
            .rate(new BigDecimal("1.07783"))
            .validFrom(localDate)
            .build();

    assertThat(exchangeRateEntity).isNotNull();

    var exchangeRateDto = mapper.map(exchangeRateEntity);

    assertThat(exchangeRateDto).isNotNull();
    assertThat(exchangeRateDto.getBaseCurrency()).isEqualTo("EUR");
    assertThat(exchangeRateDto.getCurrency()).isEqualTo("USD");
    assertThat(exchangeRateDto.getRate()).isEqualByComparingTo(new BigDecimal("1.07783"));
    assertThat(exchangeRateDto.getValidFrom()).isEqualTo(localDate);

    exchangeRateEntity = mapper.map(exchangeRateDto);
    assertThat(exchangeRateEntity).isNotNull();
    assertThat(exchangeRateEntity.getBaseCurrency()).isEqualTo("EUR");
    assertThat(exchangeRateEntity.getCurrency()).isEqualTo("USD");
    assertThat(exchangeRateEntity.getRate()).isEqualByComparingTo(new BigDecimal("1.07783"));
    assertThat(exchangeRateEntity.getValidFrom()).isEqualTo(localDate);
  }

  @Test
  @DisplayName("Transaction entity to dto layer mapping test and dto to entity layer mapping test.")
  void transactionEntityToDtoAndDtoToEntityTest() {

    var guid = UUID.randomUUID();
    var accGuid = UUID.randomUUID();

    var transactionEntity =
        TransactionEntity.builder()
            .id(1L)
            .guid(guid)
            .accGuid(accGuid)
            .beginAmount(BigDecimal.ZERO)
            .trxAmount(BigDecimal.TEN)
            .trxCurrency("EUR")
            .endAmount(BigDecimal.TEN)
            .trxType(TransactionTypeEnum.DEBIT.getVal())
            .build();

    assertThat(transactionEntity).isNotNull();

    var transactionDto = mapper.map(transactionEntity);

    assertThat(transactionDto).isNotNull();
    assertThat(transactionDto.getGuid()).isEqualTo(guid);
    assertThat(transactionDto.getAccGuid()).isEqualTo(accGuid);
    assertThat(transactionDto.getBeginAmount()).isEqualTo(BigDecimal.ZERO);
    assertThat(transactionDto.getTrxCurrency()).isEqualTo("EUR");
    assertThat(transactionDto.getEndAmount()).isEqualTo(BigDecimal.TEN);
    assertThat(transactionDto.getTrxType()).isEqualTo(TransactionTypeEnum.DEBIT);

    transactionEntity = mapper.map(transactionDto);
    assertThat(transactionEntity.getGuid()).isEqualTo(guid);
    assertThat(transactionEntity.getAccGuid()).isEqualTo(accGuid);
    assertThat(transactionEntity.getBeginAmount()).isEqualTo(BigDecimal.ZERO);
    assertThat(transactionEntity.getTrxCurrency()).isEqualTo("EUR");
    assertThat(transactionEntity.getEndAmount()).isEqualTo(BigDecimal.TEN);
    assertThat(transactionEntity.getTrxType()).isEqualTo(TransactionTypeEnum.DEBIT.getVal());
  }
}
