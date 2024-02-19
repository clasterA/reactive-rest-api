/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.api;

import com.reactive.rest.config.BaseIntegrationTest;
import com.reactive.rest.dto.Client;
import com.reactive.rest.enums.ClientStatusEnum;
import com.reactive.rest.utils.CommonUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientsApiTest extends BaseIntegrationTest {

  private String createClientJson;

  @BeforeAll
  @DisplayName("Initialize clients test variables")
  public void init() {

    createClientJson = CommonUtils.getResourceFileAsString("clients/createClient.json");
    Assertions.assertThat(createClientJson).isNotNull();
  }

  @Test
  @Order(1)
  @DisplayName("Create client, from route: POST path = /client, application/json")
  void shouldCreateNewClientExistJsonTest() {

    webClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("/client").build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(createClientJson)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Client.class)
        .value(
            client -> {
              Assertions.assertThat(client.getGuid()).isNotNull();
              Assertions.assertThat(client.getName()).isEqualTo("TEST-AAA");
              Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.ACTIVE);
            });
  }

  @Test
  @Order(2)
  @DisplayName("Get existing client, from route: GET path = /client, application/json")
  void shouldGetExistingClientJsonTest() {

    webClient()
        .get()
        .uri(uriBuilder -> uriBuilder.path("/client/" + clientList.getFirst().getGuid()).build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Client.class)
        .value(
            client -> {
              Assertions.assertThat(client.getGuid()).isEqualTo(clientList.getFirst().getGuid());
              Assertions.assertThat(client.getName()).isEqualTo("TestClient");
              Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.ACTIVE);
            });
  }

  @Test
  @Order(3)
  @DisplayName("Remove existing client, from route: DELETE path = /client, application/json")
  void shouldRemoveExistingClientJsonTest() {

    webClient()
        .delete()
        .uri(uriBuilder -> uriBuilder.path("/client/" + clientList.getFirst().getGuid()).build())
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .expectStatus()
        .isAccepted()
        .expectBody(Client.class)
        .value(
            client -> {
              Assertions.assertThat(client.getGuid()).isEqualTo(clientList.getFirst().getGuid());
              Assertions.assertThat(client.getName()).isEqualTo("TestClient");
              Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.CLOSED);
            });
  }
}
