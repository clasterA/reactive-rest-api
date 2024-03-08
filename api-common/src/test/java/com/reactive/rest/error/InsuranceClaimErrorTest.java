/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.reactive.rest.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InsuranceClaimErrorTest extends BaseIntegrationTest {

  @Autowired CommonListOfError commonListOfError;

  @Test
  @DisplayName("Message error test.")
  void MessageErrorTest() {

    var errorMessage =
        ErrorMessage.builder()
            .httpCode(CommonErrors.BAD_REQUEST.getHttpCode())
            .name("BAD_REQUEST")
            .description("description")
            .build();

    assertThat(errorMessage).isNotNull();
    assertThat(errorMessage.getHttpCode()).isEqualTo(CommonErrors.BAD_REQUEST.getHttpCode());
    assertThat(errorMessage.getName()).isEqualTo("BAD_REQUEST");
    assertThat(errorMessage.getDescription()).isEqualTo("description");
  }

  @Test
  @DisplayName("Exception error test.")
  void ExceptionTest() {

    var errorMessage =
        ErrorMessage.builder()
            .httpCode(CommonErrors.BAD_REQUEST.getHttpCode())
            .name("BAD_REQUEST")
            .description("Test exception")
            .build();

    assertThat(errorMessage).isNotNull();
    assertThat(errorMessage.getHttpCode()).isEqualTo(CommonErrors.BAD_REQUEST.getHttpCode());
    assertThat(errorMessage.getName()).isEqualTo("BAD_REQUEST");
    assertThat(errorMessage.getDescription()).isEqualTo("Test exception");

    var exception = new CommonException(errorMessage, (new Throwable()));
    assertThat(exception).isNotNull();
    assertThat(exception.getError()).isEqualTo(errorMessage);
    assertThat(exception.getMessage()).isEqualTo("Test exception");
  }

  @Test
  @DisplayName("Bad request error test.")
  void badRequestErrorTest() {

    Exception exception =
        assertThrows(
            Exception.class,
            () -> commonListOfError.badRequestError("Test resource", "Error message").block());

    assertThat(exception).isNotNull();
    assertThat(exception.getMessage())
        .isEqualTo("Bad request. Resource = Test resource, Error = Error message");
  }

  @Test
  @DisplayName("Bad request throwable error test.")
  void badRequestErrorThrowableTest() {

    Exception exception =
        assertThrows(
            Exception.class,
            () ->
                commonListOfError
                    .badRequestError("Test resource", new Throwable("Error message"))
                    .block());

    assertThat(exception).isNotNull();
    assertThat(exception.getMessage())
        .isEqualTo("Bad request. Resource = Test resource, Error = Error message");
  }
}
