/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.config;

import jakarta.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration
public class WebExceptionHandlerConfig {

  @Bean
  public WebExceptionHandler exceptionHandler() {
    return (ServerWebExchange exchange, Throwable ex) -> {
      if (ex instanceof ConstraintViolationException error) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        byte[] bytes = error.getMessage().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
      } else {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        byte[] bytes = ex.getMessage().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
      }
    };
  }
}
