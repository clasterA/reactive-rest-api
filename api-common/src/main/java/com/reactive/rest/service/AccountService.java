/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateClientAccountCommand;
import com.reactive.rest.dto.Account;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
public interface AccountService {

  @NotNull
  Mono<Account> createNewClientAccount(@Valid CreateClientAccountCommand command);

  @NotNull
  Mono<Account> getAccountByGuid(@NotNull UUID accGuid);

  @NotNull
  Mono<List<Account>> getClientAccountList(@NotNull UUID clientGuid);
}
