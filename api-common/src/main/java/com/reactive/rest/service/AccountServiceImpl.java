/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateClientAccountCommand;
import com.reactive.rest.dto.Account;
import com.reactive.rest.mapper.CommonMapper;
import com.reactive.rest.repository.AccountEntity;
import com.reactive.rest.repository.AccountRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final CommonMapper mapper;

  @Override
  public @NotNull Mono<Account> createNewClientAccount(CreateClientAccountCommand command) {

    return accountRepository
        .save(createAccountRequest(command))
        .onErrorResume(
            ex ->
                Mono.error(
                    () -> new RuntimeException("Create client account error: " + ex.getMessage())))
        .map(mapper::map);
  }

  @Override
  public @NotNull Mono<List<Account>> getClientAccountList(@NotNull UUID clientGuid) {

    return Mono.defer(
            () ->
                accountRepository
                    .getClientAccountList(clientGuid)
                    .collectList()
                    .map(mapper::mapAccountList))
        .doOnEach(accountList -> log.debug("Client account data : {}", accountList))
        .switchIfEmpty(
            Mono.error(() -> new RuntimeException("Not found any accounts for this client")));
  }

  private AccountEntity createAccountRequest(CreateClientAccountCommand command) {
    return AccountEntity.builder()
        .guid(UUID.randomUUID())
        .clientGuid(command.getClientGuid())
        .clientName(command.getClientName())
        .name(command.getName())
        .currency(command.getCurrency())
        .build();
  }
}
