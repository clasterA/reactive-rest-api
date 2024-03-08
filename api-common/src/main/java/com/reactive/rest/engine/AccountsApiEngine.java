/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.engine;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;

import com.reactive.rest.command.CreateClientAccountCommand;
import com.reactive.rest.dto.Account;
import com.reactive.rest.dto.AccountList;
import com.reactive.rest.service.AccountService;
import com.reactive.rest.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountsApiEngine {

  private final AccountService accountService;
  private final CommonUtils commonUtils;

  protected Mono<Account> createAccount(ServerRequest serverRequest) {

    return serverRequest
        .body(toMono(CreateClientAccountCommand.class))
        .flatMap(accountService::createNewClientAccount);
  }

  protected Mono<AccountList> getAccountList(ServerRequest serverRequest) {

    return Mono.just(serverRequest)
        .flatMap(commonUtils::getUrlId)
        .flatMap(accountService::getClientAccountList)
        .flatMap(
            accounts -> {
              var accountList = new AccountList();
              accountList.setAccounts(accounts);
              return Mono.just(accountList);
            });
  }
}
