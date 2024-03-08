/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api.handler;

import com.reactive.rest.dto.Client;
import com.reactive.rest.dto.ClientList;
import com.reactive.rest.engine.ClientsApiEngine;
import com.reactive.rest.service.ClientService;
import com.reactive.rest.utils.CommonUtils;
import java.net.URI;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ClientsApiHandler extends ClientsApiEngine {

  public ClientsApiHandler(ClientService clientService, CommonUtils commonUtils) {
    super(clientService, commonUtils);
  }

  /**
   * Operation create new client without accounts
   *
   * @return - all the products info as part of ServerResponse
   */
  @SneakyThrows
  public Mono<ServerResponse> createClient(ServerRequest serverRequest) {

    return ServerResponse.created(new URI("http://localhost"))
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.createNewClient(serverRequest), Client.class);
  }

  /**
   * Operation return client with all accounts (full client structure)
   *
   * @return - all the products info as part of ServerResponse
   */
  public Mono<ServerResponse> getClient(ServerRequest serverRequest) {

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.getExistingClient(serverRequest), Client.class);
  }

  /**
   * Operation return client list
   *
   * @return - all the products info as part of ServerResponse
   */
  public Mono<ServerResponse> getClients(ServerRequest serverRequest) {

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.getClientList(serverRequest), ClientList.class);
  }

  /**
   * Operation change client status to closed, physical from database nothing deleted
   *
   * @return - all the products info as part of ServerResponse
   */
  public Mono<ServerResponse> removeClient(ServerRequest serverRequest) {

    return ServerResponse.accepted()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.removeExistingClient(serverRequest), Client.class);
  }
}
