/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import com.reactive.rest.api.handler.TransactionsApiHandler;
import com.reactive.rest.dto.Client;
import com.reactive.rest.dto.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.UUID;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TransactionsApi {

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/transaction",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.POST,
        operation =
            @Operation(
                operationId = "createTransaction",
                summary = "Create new transaction",
                description =
                    "Operation create new debit / credit transaction. A debit entry in an account represents a transfer of value to that account, and a credit entry represents a transfer from the account.",
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = "Created",
                      content = @Content(schema = @Schema(implementation = Transaction.class))),
                  @ApiResponse(responseCode = "400", description = "Bad Request")
                }))
  })
  @RequestMapping(
      value = "/transaction",
      produces = {"application/json"},
      consumes = {"application/json"},
      method = RequestMethod.POST)
  @SuppressWarnings({"all"})
  public RouterFunction<ServerResponse> createTransaction(TransactionsApiHandler handler) {
    return RouterFunctions.route(
        POST("/transaction").and(accept(MediaType.APPLICATION_JSON)), handler::createTransaction);
  }

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/transaction/account/{id}",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.GET,
        params = {"page"},
        operation =
            @Operation(
                operationId = "getTransactionsForAccount",
                summary = "Get transaction list for account",
                description = "Operation return paginated transaction list for selected account",
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "OK",
                      content = @Content(schema = @Schema(implementation = Transaction.class))),
                  @ApiResponse(responseCode = "400", description = "Bad Request")
                }))
  })
  @RequestMapping(
      value = "/transaction/account/{id}",
      produces = {"application/json"},
      consumes = {"application/json"},
      method = RequestMethod.GET)
  @SuppressWarnings({"all"})
  public RouterFunction<ServerResponse> getTransactionsForAccount(TransactionsApiHandler handler) {
    return RouterFunctions.route(
        GET("/transaction/account/{id}")
            .and(queryParam("page", t -> true).and(accept(MediaType.APPLICATION_JSON))),
        handler::getTransactionsForAccount);
  }
}
