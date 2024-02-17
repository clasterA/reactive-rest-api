/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import com.reactive.rest.api.handler.AccountsApiHandler;
import com.reactive.rest.dto.Client;
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
public class AccountsApi {

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/account/client/{id}",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.POST,
        operation =
            @Operation(
                operationId = "createNewClientAccount",
                summary = "Create new client account",
                description = "Operation create new client account",
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = "Created",
                      content = @Content(schema = @Schema(implementation = UUID.class))),
                  @ApiResponse(responseCode = "400", description = "Bad Request")
                }))
  })
  @RequestMapping(
      value = "/account/client/{id}",
      produces = {"application/json"},
      consumes = {"application/json"},
      method = RequestMethod.POST)
  @SuppressWarnings({"all"})
  public RouterFunction<ServerResponse> createNewClientAccount(AccountsApiHandler handler) {
    return RouterFunctions.route(
        POST("/account/client/{id}").and(accept(MediaType.APPLICATION_JSON)),
        handler::createNewClientAccount);
  }

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/account/client/{id}",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.GET,
        operation =
            @Operation(
                operationId = "getClientAccountList",
                summary = "Get client account list",
                description = "Operation return client account list by client guid",
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "OK",
                      content = @Content(schema = @Schema(implementation = Client.class))),
                  @ApiResponse(responseCode = "400", description = "Bad Request")
                }))
  })
  @RequestMapping(
      value = "/account/client/{id}",
      produces = {"application/json"},
      consumes = {"application/json"},
      method = RequestMethod.GET)
  @SuppressWarnings({"all"})
  public RouterFunction<ServerResponse> getClientAccountList(AccountsApiHandler handler) {
    return RouterFunctions.route(
        GET("/account/client/{id}").and(accept(MediaType.APPLICATION_JSON)),
        handler::getClientAccountList);
  }
}
