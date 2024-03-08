/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.error;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
public interface CommonListOfError {

  <T> Mono<T> badRequestError(@NotBlank String resourceName, @NotBlank String errorMessage);

  <T> Mono<T> badRequestError(String resourceName, Throwable ex);
}
