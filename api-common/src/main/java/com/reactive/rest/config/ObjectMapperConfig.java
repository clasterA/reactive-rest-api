/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

  @Bean(name = "restApiObjectMapper")
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper =
        JsonMapper.builder()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .build();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setDefaultPropertyInclusion(
        JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.ALWAYS));
    return objectMapper;
  }
}
