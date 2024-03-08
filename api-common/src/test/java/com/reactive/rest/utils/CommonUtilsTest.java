/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.reactive.rest.config.BaseIntegrationTest;
import com.reactive.rest.error.CommonException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommonUtilsTest extends BaseIntegrationTest {

  @Autowired CommonUtils commonUtils;

  private ServerRequest serverRequest;

  @BeforeAll
  @DisplayName("Test variable initialization")
  public void setUp() {
    MockServerHttpRequest request =
        MockServerHttpRequest.post("http://test.com").header("header", "value").build();
    MockServerWebExchange exchange = MockServerWebExchange.from(request);
    serverRequest =
        ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
  }

  @Test
  @DisplayName("Should return error: Error getting url id attribute")
  void routerGetUrlIdReturnErrorTest() {

    Exception exception =
        assertThrows(Exception.class, () -> commonUtils.getUrlId(serverRequest).block());

    assertThat(exception).isInstanceOf(CommonException.class);
    if (exception instanceof CommonException error) {
      assertThat(error.getError().getName()).isEqualTo("Error getting url id attribute");
    }
  }

  @Test
  @DisplayName("Should return error when resource not found")
  void commonUtilsResourceNotFoundTest() {

    Exception exception =
        assertThrows(Exception.class, () -> CommonUtils.getResourceFileAsString("error_resource"));

    assertThat(exception).isNotNull();
    assertThat(exception.getMessage()).isEqualTo("resource not found");
  }
}
