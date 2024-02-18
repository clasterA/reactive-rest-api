/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateClientAccountCommand;
import com.reactive.rest.dto.Account;
import com.reactive.rest.error.CommonListOfError;
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
  private final ClientService clientService;
  private final CommonListOfError commonListOfError;
  private final CommonMapper mapper;

  @Override
  public @NotNull Mono<Account> createNewClientAccount(CreateClientAccountCommand command) {

    return clientService
        .getClientByGuid(command.getClientGuid())
        .flatMap(
            client -> {
              command.setClientName(client.getName());
              return accountRepository
                  .save(createAccountRequest(command))
                  .onErrorResume(
                      ex ->
                          commonListOfError.badRequestError(
                              "Create new client account", ex.getMessage()))
                  .map(mapper::map);
            });
  }

  @Override
  public @NotNull Mono<Account> getAccountByGuid(@NotNull UUID accGuid) {

    return accountRepository
        .findByGuid(accGuid)
        .map(mapper::map)
        .switchIfEmpty(
            commonListOfError.badRequestError("Get account by guid", "Account not found"));
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
            commonListOfError.badRequestError(
                "Get client account list", "Not found any account for client"));
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
