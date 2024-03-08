/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.config;

import com.reactive.rest.error.CommonException;
import jakarta.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration
public class WebExceptionHandlerConfig {

  /**
   * DefaultErrorWebExceptionHandler - provided by Spring Boot for error handling: Ordered -1.
   * ResponseStatusExceptionHandler - provided by Spring Framework for error handling: Ordered 0.
   * Our error handling must calling first: @Order(-2)
   */
  @Bean
  @Order(-2)
  public WebExceptionHandler exceptionHandler() {
    return (ServerWebExchange exchange, Throwable ex) -> {
      if (ex instanceof CommonException error) {
        exchange.getResponse().setStatusCode(HttpStatus.valueOf(error.getError().getHttpCode()));
        switch (error.getError().getHttpCode()) {
          case 400 -> {
            byte[] bytes = ex.getMessage().getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Flux.just(buffer));
          }
          default -> {
            return exchange.getResponse().setComplete();
          }
        }
      }
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
