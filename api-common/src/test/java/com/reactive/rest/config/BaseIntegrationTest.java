/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.config;

import com.reactive.rest.command.CreateClientAccountCommand;
import com.reactive.rest.command.CreateClientCommand;
import com.reactive.rest.dto.Account;
import com.reactive.rest.dto.Client;
import com.reactive.rest.enums.ClientStatusEnum;
import com.reactive.rest.repository.AccountRepository;
import com.reactive.rest.repository.ClientRepository;
import com.reactive.rest.repository.TransactionRepository;
import com.reactive.rest.service.AccountService;
import com.reactive.rest.service.ClientService;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@SpringBootTest(
    classes = TestApplication.class,
    properties = "classpath:application-itest.yml",
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(resolver = TestActiveProfilesResolver.class)
public abstract class BaseIntegrationTest extends Assertions {

  {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Autowired private ApplicationContext applicationContext;

  @Autowired ClientService clientService;

  @Autowired ClientRepository clientRepository;

  @Autowired AccountService accountService;

  @Autowired AccountRepository accountRepository;

  @Autowired TransactionRepository transactionRepository;

  @LocalServerPort private Integer serverPort;
  protected List<Client> clientList = new ArrayList<>();
  protected List<Account> accountList = new ArrayList<>();

  protected WebTestClient webClient() {
    return WebTestClient.bindToApplicationContext(applicationContext)
        .configureClient()
        .responseTimeout(Duration.ofSeconds(30))
        .baseUrl("http://localhost:" + serverPort)
        .build();
  }

  protected static final PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(
              DockerImageName.parse("postgres:latest").asCompatibleSubstituteFor("postgres"))
          .withDatabaseName("rest_api_db")
          .withUsername("postgres")
          .withPassword("postgres")
          .withNetwork(Network.newNetwork())
          .withNetworkAliases("postgres")
          .withReuse(true);

  @DynamicPropertySource
  static void registerSQLProperties(DynamicPropertyRegistry registry) {

    postgreSQLContainer.setWaitStrategy(
        new LogMessageWaitStrategy()
            .withRegEx(".*database system is ready to accept connections.*\\s")
            .withTimes(1)
            .withStartupTimeout(Duration.of(30, ChronoUnit.SECONDS)));
    postgreSQLContainer.start();

    registry.add("spring.r2dbc.url", BaseIntegrationTest::getR2dbUrl);
    registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
    registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
    registry.add("spring.r2dbc.driver-class-name", postgreSQLContainer::getDriverClassName);
    registry.add("spring.liquibase.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.liquibase.user", postgreSQLContainer::getUsername);
    registry.add("spring.liquibase.password", postgreSQLContainer::getPassword);
    registry.add("spring.liquibase.parameters.rds_db_name", () -> "rest_api_db");
    registry.add("spring.liquibase.parameters.rds_username", postgreSQLContainer::getUsername);
    registry.add("spring.liquibase.contexts", () -> "!prod");
  }

  static String getR2dbUrl() {

    return "r2dbc:postgresql://"
        + postgreSQLContainer.getHost()
        + ":"
        + postgreSQLContainer.getFirstMappedPort().toString()
        + "/"
        + postgreSQLContainer.getDatabaseName()
        + "?loggerLevel=OFF";
  }

  @BeforeAll
  @DisplayName("Initialize db for integration testing")
  void setUp() {

    var clientCommand = CreateClientCommand.builder().name("TestClient").build();

    var client = clientService.createClient(clientCommand).block();

    Assertions.assertThat(client).isNotNull();
    Assertions.assertThat(client.getGuid()).isNotNull();
    Assertions.assertThat(client.getName()).isEqualTo("TestClient");
    Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.ACTIVE);

    clientList.add(client);

    var accountCommand =
        CreateClientAccountCommand.builder()
            .clientGuid(clientList.getFirst().getGuid())
            .name("Euro account")
            .currency("EUR")
            .build();

    var account = accountService.createNewClientAccount(accountCommand).block();

    Assertions.assertThat(account).isNotNull();
    Assertions.assertThat(account.getGuid()).isNotNull();
    Assertions.assertThat(account.getClientGuid()).isEqualTo(clientList.getFirst().getGuid());
    Assertions.assertThat(account.getName()).isEqualTo("Euro account");
    Assertions.assertThat(account.getClientName()).isEqualTo("TestClient");
    Assertions.assertThat(account.getCurrency()).isEqualTo("EUR");

    accountList.add(account);

    accountCommand =
        CreateClientAccountCommand.builder()
            .clientGuid(clientList.getFirst().getGuid())
            .name("Usd account")
            .currency("USD")
            .build();

    account = accountService.createNewClientAccount(accountCommand).block();

    Assertions.assertThat(account).isNotNull();
    Assertions.assertThat(account.getGuid()).isNotNull();
    Assertions.assertThat(account.getClientGuid()).isEqualTo(clientList.getFirst().getGuid());
    Assertions.assertThat(account.getName()).isEqualTo("Usd account");
    Assertions.assertThat(account.getClientName()).isEqualTo("TestClient");
    Assertions.assertThat(account.getCurrency()).isEqualTo("USD");

    accountList.add(account);

    accountCommand =
        CreateClientAccountCommand.builder()
            .clientGuid(clientList.getFirst().getGuid())
            .name("Gbp account")
            .currency("GBP")
            .build();

    account = accountService.createNewClientAccount(accountCommand).block();

    Assertions.assertThat(account).isNotNull();
    Assertions.assertThat(account.getGuid()).isNotNull();
    Assertions.assertThat(account.getClientGuid()).isEqualTo(clientList.getFirst().getGuid());
    Assertions.assertThat(account.getName()).isEqualTo("Gbp account");
    Assertions.assertThat(account.getClientName()).isEqualTo("TestClient");
    Assertions.assertThat(account.getCurrency()).isEqualTo("GBP");

    accountList.add(account);

    clientCommand = CreateClientCommand.builder().name("TestClient2").build();

    client = clientService.createClient(clientCommand).block();

    Assertions.assertThat(client).isNotNull();
    Assertions.assertThat(client.getGuid()).isNotNull();
    Assertions.assertThat(client.getName()).isEqualTo("TestClient2");
    Assertions.assertThat(client.getStatus()).isEqualTo(ClientStatusEnum.ACTIVE);

    clientList.add(client);

    accountCommand =
        CreateClientAccountCommand.builder()
            .clientGuid(clientList.getLast().getGuid())
            .name("Euro account")
            .currency("EUR")
            .build();

    account = accountService.createNewClientAccount(accountCommand).block();

    Assertions.assertThat(account).isNotNull();
    Assertions.assertThat(account.getGuid()).isNotNull();
    Assertions.assertThat(account.getClientGuid()).isEqualTo(clientList.getLast().getGuid());
    Assertions.assertThat(account.getName()).isEqualTo("Euro account");
    Assertions.assertThat(account.getClientName()).isEqualTo("TestClient2");
    Assertions.assertThat(account.getCurrency()).isEqualTo("EUR");

    accountList.add(account);
  }

  @AfterAll
  @DisplayName("Clear test db after test is completed")
  void clearSetUp() {

    transactionRepository.deleteAll().block();
    accountRepository.deleteAll().block();
    clientRepository.deleteAll().block();
  }
}
