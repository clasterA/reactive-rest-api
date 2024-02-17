/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateClientCommand;
import com.reactive.rest.dto.Client;
import com.reactive.rest.enums.ClientStatusEnum;
import com.reactive.rest.mapper.CommonMapper;
import com.reactive.rest.repository.ClientEntity;
import com.reactive.rest.repository.ClientRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final ClientRepository clientRepository;
  private final CommonMapper mapper;

  @Override
  public @NotNull Mono<Client> createClient(CreateClientCommand command) {

    return clientRepository
        .save(createClientRequest(command))
        .onErrorResume(
            ex -> Mono.error(() -> new RuntimeException("Create client error: " + ex.getMessage())))
        .map(mapper::map);
  }

  @Override
  public @NotNull Mono<Client> getClientByGuid(@NotNull UUID guid) {

    return clientRepository
        .findByGuid(guid)
        .map(mapper::map)
        .switchIfEmpty(Mono.error(() -> new RuntimeException("Client not found")));
  }

  @Override
  public @NotNull Mono<Client> removeClient(@NotNull UUID guid) {

    return clientRepository
        .removeClient(guid, ClientStatusEnum.CLOSED.getVal())
        .onErrorResume(
            ex -> Mono.error(() -> new RuntimeException("Remove client error: " + ex.getMessage())))
        .map(mapper::map);
  }

  private ClientEntity createClientRequest(CreateClientCommand command) {
    return ClientEntity.builder()
        .guid(UUID.randomUUID())
        .name(command.getName())
        .status(ClientStatusEnum.ACTIVE.getVal())
        .build();
  }
}
