/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.config;

import static org.springframework.http.MediaType.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(ServerProperties.class)
public class HttpConfig {

  @Bean
  public Jackson2JsonEncoder jackson2JsonEncoder(
      @Qualifier("restApiObjectMapper") ObjectMapper objectMapper) {
    return new Jackson2JsonEncoder(objectMapper, APPLICATION_JSON);
  }

  @Bean
  public Jackson2JsonDecoder jackson2JsonDecoder(
      @Qualifier("restApiObjectMapper") ObjectMapper objectMapper) {
    return new Jackson2JsonDecoder(objectMapper, APPLICATION_JSON);
  }

  @Bean
  public WebFluxConfigurer webFluxConfigurer(
      Jackson2JsonEncoder jackson2JsonEncoder, Jackson2JsonDecoder jackson2JsonDecoder) {
    return new WebFluxConfigurer() {
      @Override
      public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(jackson2JsonEncoder);
        configurer.defaultCodecs().jackson2JsonDecoder(jackson2JsonDecoder);
      }
    };
  }
}
