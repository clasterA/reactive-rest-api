/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api.handler;

import com.reactive.rest.dto.Account;
import com.reactive.rest.dto.AccountList;
import com.reactive.rest.engine.AccountsApiEngine;
import com.reactive.rest.service.AccountService;
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
public class AccountsApiHandler extends AccountsApiEngine {

  public AccountsApiHandler(AccountService accountService, CommonUtils commonUtils) {
    super(accountService, commonUtils);
  }

  /**
   * Operation create new client account
   *
   * @return - all the products info as part of ServerResponse
   */
  @SneakyThrows
  public Mono<ServerResponse> createNewClientAccount(ServerRequest serverRequest) {

    return ServerResponse.created(new URI("http://localhost"))
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.createAccount(serverRequest), Account.class);
  }

  /**
   * Operation return client account list by client guid
   *
   * @return - all the products info as part of ServerResponse
   */
  public Mono<ServerResponse> getClientAccountList(ServerRequest serverRequest) {

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.getAccountList(serverRequest), AccountList.class);
  }
}
