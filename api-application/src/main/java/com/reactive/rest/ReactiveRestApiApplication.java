/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@OpenAPIDefinition(
    info =
        @Info(
            title = "Resctive Rest Api",
            version = "1.0",
            description = "Documentation APIs v1.0"))
@SpringBootApplication(
    scanBasePackages = {"com.reactive.rest"},
    exclude = {
      ErrorWebFluxAutoConfiguration.class,
      LiquibaseAutoConfiguration.class,
    })
public class ReactiveRestApiApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(ReactiveRestApiApplication.class).run(args);
  }
}
