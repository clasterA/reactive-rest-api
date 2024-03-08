/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import com.reactive.rest.api.handler.ClientsApiHandler;
import com.reactive.rest.dto.Client;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class ClientsApi {

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/client",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.POST,
        operation =
            @Operation(
                operationId = "createClient",
                summary = "Create new client",
                description = "Operation create new client, with status active",
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = "Created",
                      content = @Content(schema = @Schema(implementation = Client.class))),
                  @ApiResponse(responseCode = "400", description = "Bad Request")
                }))
  })
  @RequestMapping(
      value = "/client",
      produces = {"application/json"},
      consumes = {"application/json"},
      method = RequestMethod.POST)
  @SuppressWarnings({"all"})
  public RouterFunction<ServerResponse> createClient(ClientsApiHandler handler) {
    return RouterFunctions.route(
        POST("/client").and(accept(MediaType.APPLICATION_JSON)), handler::createClient);
  }

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/client",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.GET,
        operation =
            @Operation(
                operationId = "getClientList",
                summary = "Get client list",
                description = "Operation return client list",
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "OK",
                      content = @Content(schema = @Schema(implementation = Client.class))),
                  @ApiResponse(responseCode = "400", description = "Bad Request")
                }))
  })
  @RequestMapping(
      value = "/client",
      produces = {"application/json"},
      consumes = {"application/json"},
      method = RequestMethod.GET)
  @SuppressWarnings({"all"})
  public RouterFunction<ServerResponse> getClientList(ClientsApiHandler handler) {
    return RouterFunctions.route(
        GET("/client").and(accept(MediaType.APPLICATION_JSON)), handler::getClients);
  }

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/client/{id}",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.GET,
        operation =
            @Operation(
                operationId = "getClient",
                summary = "Get client by uuid",
                description = "Operation return client",
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "OK",
                      content = @Content(schema = @Schema(implementation = Client.class))),
                  @ApiResponse(responseCode = "400", description = "Bad Request")
                }))
  })
  @RequestMapping(
      value = "/client/{id}",
      produces = {"application/json"},
      consumes = {"application/json"},
      method = RequestMethod.GET)
  @SuppressWarnings({"all"})
  public RouterFunction<ServerResponse> getClient(ClientsApiHandler handler) {
    return RouterFunctions.route(
        GET("/client/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getClient);
  }

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/client/{id}",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.DELETE,
        operation =
            @Operation(
                operationId = "removeClient",
                summary = "Remove client by uuid",
                description =
                    "Operation change client status to closed, physical from database nothing deleted",
                responses = {
                  @ApiResponse(
                      responseCode = "202",
                      description = "Accepted",
                      content = @Content(schema = @Schema(implementation = Client.class))),
                  @ApiResponse(responseCode = "400", description = "Bad Request")
                }))
  })
  @RequestMapping(
      value = "/client/{id}",
      produces = {"application/json"},
      consumes = {"application/json"},
      method = RequestMethod.DELETE)
  @SuppressWarnings({"all"})
  public RouterFunction<ServerResponse> removeClient(ClientsApiHandler handler) {
    return RouterFunctions.route(
        DELETE("/client/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::removeClient);
  }
}
