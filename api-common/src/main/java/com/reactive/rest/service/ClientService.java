/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateClientCommand;
import com.reactive.rest.dto.Client;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
public interface ClientService {

  @NotNull
  Mono<Client> createClient(@Valid CreateClientCommand command);

  @NotNull
  Mono<Client> getClientByGuid(@NotNull UUID guid);

  @NotNull
  Mono<List<Client>> getClients();

  @NotNull
  Mono<Client> removeClient(@NotNull UUID guid);
}
