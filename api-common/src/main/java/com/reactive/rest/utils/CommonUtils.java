/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.utils;

import com.reactive.rest.error.CommonException;
import com.reactive.rest.error.ErrorMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Component
public class CommonUtils {

  /**
   * Return ID - Resource object identification from url.
   *
   * @param serverRequest rest - api server request
   * @return UUID id
   */
  @SuppressWarnings("unchecked")
  public Mono<UUID> getUrlId(ServerRequest serverRequest) {

    return Mono.fromCallable(
        () -> {
          try {
            if (serverRequest
                .attribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)
                .isPresent()) {

              return UUID.fromString(
                  ((Map<String, String>)
                          serverRequest
                              .attribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)
                              .get())
                      .get("id"));
            } else {
              throw new CommonException(
                  ErrorMessage.builder()
                      .httpCode(HttpStatus.BAD_REQUEST.value())
                      .name("Error getting url id attribute")
                      .build());
            }
          } catch (Exception ex) {
            throw new CommonException(
                ErrorMessage.builder()
                    .httpCode(HttpStatus.BAD_REQUEST.value())
                    .name("Error getting url id attribute")
                    .build(),
                ex);
          }
        });
  }

  public static String getResourceFileAsString(String fileName) {
    InputStream is = getResourceFileAsInputStream(fileName);
    if (is != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
      var result = reader.lines().collect(Collectors.joining(System.lineSeparator()));
      try {
        reader.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return result;
    } else {
      throw new RuntimeException("resource not found");
    }
  }

  private static InputStream getResourceFileAsInputStream(String fileName) {
    ClassLoader classLoader = CommonUtils.class.getClassLoader();
    return classLoader.getResourceAsStream(fileName);
  }
}
