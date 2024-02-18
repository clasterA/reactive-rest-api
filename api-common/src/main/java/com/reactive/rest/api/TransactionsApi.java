/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.reactive.rest.api.handler.TransactionsApiHandler;
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
                      content = @Content(schema = @Schema(implementation = UUID.class))),
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
}
