/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.engine;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;

import com.reactive.rest.command.CreateClientCommand;
import com.reactive.rest.dto.Client;
import com.reactive.rest.service.ClientService;
import com.reactive.rest.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientsApiEngine {

  private final ClientService clientService;

  protected Mono<Client> createNewClient(ServerRequest serverRequest) {

    return serverRequest
        .body(toMono(CreateClientCommand.class))
        .flatMap(clientService::createClient);
  }

  protected Mono<Client> getExistingClient(ServerRequest serverRequest) {

    return Mono.just(serverRequest)
        .flatMap(CommonUtils::getUrlId)
        .flatMap(clientService::getClientByGuid);
  }

  protected Mono<Client> removeExistingClient(ServerRequest serverRequest) {

    return Mono.just(serverRequest)
        .flatMap(CommonUtils::getUrlId)
        .flatMap(clientService::removeClient);
  }
}
