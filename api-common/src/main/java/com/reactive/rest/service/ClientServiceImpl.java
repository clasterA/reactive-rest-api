/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.service;

import com.reactive.rest.command.CreateClientCommand;
import com.reactive.rest.dto.Client;
import com.reactive.rest.enums.ClientStatusEnum;
import com.reactive.rest.error.CommonListOfError;
import com.reactive.rest.mapper.CommonMapper;
import com.reactive.rest.repository.ClientEntity;
import com.reactive.rest.repository.ClientRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final ClientRepository clientRepository;
  private final CommonListOfError commonListOfError;
  private final CommonMapper mapper;

  @Override
  @Transactional
  public @NotNull Mono<Client> createClient(CreateClientCommand command) {

    return clientRepository
        .save(createClientRequest(command))
        .onErrorResume(ex -> commonListOfError.badRequestError("Create client", ex.getMessage()))
        .map(mapper::map);
  }

  @Override
  public @NotNull Mono<Client> getClientByGuid(@NotNull UUID guid) {

    return clientRepository
        .findByGuid(guid)
        .map(mapper::map)
        .switchIfEmpty(commonListOfError.badRequestError("Get client by guid", "Client not found"));
  }

  @Override
  public @NotNull Mono<List<Client>> getClients() {

    return Mono.defer(
            () -> clientRepository.getClientList().collectList().map(mapper::mapClientList))
        .doOnEach(clientList -> log.debug("Client data : {}", clientList))
        .switchIfEmpty(commonListOfError.badRequestError("Get clients", "Clients not found"));
  }

  @Override
  @Transactional
  public @NotNull Mono<Client> removeClient(@NotNull UUID guid) {

    return clientRepository
        .removeClient(guid, ClientStatusEnum.CLOSED.getVal())
        .onErrorResume(ex -> commonListOfError.badRequestError("Remove client", ex.getMessage()))
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
