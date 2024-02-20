/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class CommonUtils {

  /**
   * Return ID - Resource object identification from url.
   *
   * @param serverRequest rest - api server request
   * @return UUID id
   */
  @SuppressWarnings("unchecked")
  public static Mono<UUID> getUrlId(ServerRequest serverRequest) {

    return Mono.fromCallable(
        () -> {
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
            return UUID.fromString("");
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
