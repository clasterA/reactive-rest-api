/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableR2dbcRepositories(value = "com.reactive.rest")
@EnableTransactionManagement
class DatabaseConfig {}
