/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.error;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public enum CommonErrors {
  BAD_REQUEST(400);

  private int httpCode;

  public int getHttpCode() {
    return httpCode;
  }

  public <T> Mono<T> toError(String message) {
    return Mono.error(() -> new CommonException(this.toMessageError(message)));
  }

  public ErrorMessage toMessageError(String description) {
    return new ErrorMessage(this.httpCode, this.name(), description);
  }
}
