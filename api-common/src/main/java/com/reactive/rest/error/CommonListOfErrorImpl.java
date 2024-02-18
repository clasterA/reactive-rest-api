/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.error;

import static com.reactive.rest.error.CommonErrors.BAD_REQUEST;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CommonListOfErrorImpl implements CommonListOfError {

  @Override
  public <T> Mono<T> badRequestError(String resourceName, String errorMessage) {
    return Mono.fromSupplier(
            () ->
                String.format("Bad request. Resource = %s, Error = %s", resourceName, errorMessage))
        .flatMap(BAD_REQUEST::toError);
  }
}
