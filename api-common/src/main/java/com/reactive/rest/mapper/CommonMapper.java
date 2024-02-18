/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.mapper;

import com.reactive.rest.dto.Account;
import com.reactive.rest.dto.Client;
import com.reactive.rest.dto.Transaction;
import com.reactive.rest.enums.ClientStatusEnum;
import com.reactive.rest.enums.TransactionTypeEnum;
import com.reactive.rest.repository.AccountEntity;
import com.reactive.rest.repository.ClientEntity;
import com.reactive.rest.repository.TransactionEntity;
import java.util.List;
import org.mapstruct.*;

@Mapper(
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface CommonMapper {

  Account map(AccountEntity source);

  AccountEntity map(Account source);

  @Mapping(target = "trxType", expression = "java(getTransactionTypeEnumFromString(source))")
  Transaction map(TransactionEntity source);

  @Mapping(target = "trxType", expression = "java(getTransactionTypeEnumFromValue(source))")
  TransactionEntity map(Transaction source);

  @Mapping(target = "status", expression = "java(getClientStatusEnumFromString(source))")
  Client map(ClientEntity source);

  @Mapping(target = "status", expression = "java(getClientStatusEnumFromValue(source))")
  ClientEntity map(Client source);

  default ClientStatusEnum getClientStatusEnumFromString(ClientEntity source) {
    return ClientStatusEnum.fromString(source.getStatus());
  }

  default String getClientStatusEnumFromValue(Client source) {
    return source.getStatus().getVal();
  }

  default TransactionTypeEnum getTransactionTypeEnumFromString(TransactionEntity source) {
    return TransactionTypeEnum.fromString(source.getTrxType());
  }

  default String getTransactionTypeEnumFromValue(Transaction source) {
    return source.getTrxType().getVal();
  }

  List<Account> mapAccountList(List<AccountEntity> sourceList);

  List<Client> mapClientList(List<ClientEntity> sourceList);

  List<Transaction> mapTransactionList(List<TransactionEntity> sourceList);
}
