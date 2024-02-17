/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = {"com.reactive.*"})
@EnableR2dbcRepositories(value = "com.reactive.*")
@ComponentScan(basePackages = {"com.reactive.*"})
public class TestApplication extends SpringBootServletInitializer {}
